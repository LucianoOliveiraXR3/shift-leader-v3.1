package br.com.crearesistemas.shift_leader.service

import android.app.Application
import android.util.Log
import br.com.crearesistemas.shift_leader.db_service.model.*
import br.com.crearesistemas.shift_leader.db_service.viewmodel.*
import br.com.crearesistemas.shift_leader.dto.AuthResponseDto
import br.com.crearesistemas.shift_leader.dto.UserRequestDto
import br.com.crearesistemas.shift_leader.singleton.MessageSingleton
import br.com.crearesistemas.socket.client.service.MachineStatusSingleton
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.BufferedReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.Date
import java.util.concurrent.TimeUnit


/**
 *
 */
class APIService(var application: Application )
{
    private lateinit var token : String
    private var userRequest = UserRequestDto("master", "TddUBRjP5UXBZafrgR")

    //val createBaseUrl = "https://april.crearecloud.com.br/api/shift-leader/"
    private val createBaseUrl = "https://qa-april.crearecloud.com.br/api/shift-leader/v3/"
    //private val createBaseUrl = "http://10.0.0.254:8095/api/shift-leader/"


    /**
     *
     */
    companion object
    {
        private val TAG = APIService::class.java.simpleName
    }


    /**
     *
     */
    private fun client(): OkHttpClient
    {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    private fun gson(): Gson = GsonBuilder().create()


    /**
     *
     */
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(createBaseUrl)
        .client(client())
        .addConverterFactory(GsonConverterFactory.create(gson()))
        .build()


    /**
     *
     */
    private val api: ApiInterface = retrofit.create(ApiInterface::class.java)


    /**
     *
     */
    fun syncToCloud()
    {
        Log.d(TAG, "####################################################################################################################")
        Log.d(TAG, "# fun syncToCloud(context: Context)::START")

        // TODO - ALERT LOGIN FAIL / API OFFLINE / TABLET OFFLINE
        if( !this.cloudLogin() ) {
            this.logMessage("LOGIN FAILED INTO API")
            return
        }

        // SEND DATA FROM CLOUD
        this.sendAppointments()
        this.sendAppointmentSemiMechanized()
        this.sendChecklistSaved()
        this.sendDriverLogin()
        this.sendMachineSetup()
        this.sendProductionHarvester()
        this.sendTelemetry()
        this.sendTelemetryAlert()

        // GET DATA FROM CLOUD
        this.getMachineList()
        this.getUserList()
        this.getDriverList()
        this.getActivityTypeList()
        this.getEquipmentList()
        this.getMechanizationTypeList()
        this.getChecklistItemsList()
        this.getFarmSectorList()
        this.getFarmCompartmentList()
        this.getAppointmentGroupList()
        this.getAppointmentList()
        this.getAppointmentMachineTypeList()
        this.getAppointmentDataList()
        this.getTelemetryMonitoringList()
        this.getMachineGroupList()

        this.logMessage("ALL DATA SYNCHRONIZED!!")
        this.logMessage("FINISHED SUCCESSFUL")
        MachineStatusSingleton.lastSyncCloudDate = Date()
    }

    /**
     *
     */
    private fun cloudLogin(): Boolean
    {
        Log.d(TAG, "# private fun cloudLogin(): Boolean")

        try {
            val loginResponse = this.api.login(this.userRequest).execute()

            if (!loginResponse.isSuccessful) {
                return false
            }

            val loginBody = loginResponse.body()
            val jsonReceived = loginBody.toString().trim()

            if (jsonReceived.isEmpty()) {
                this.logMessage("LOGIN FAILED: EMPTY CONTENT")
                return false
            }

            this.token = loginBody!!.token.toString().trim()
            if (this.token.isEmpty()) {
                this.logMessage("LOGIN FAILED: INVALID TOKEN")
                return false
            }

            this.logMessage("API LOGIN SUCESS!!")
            return true
        }
        catch (e : Exception) {
            e.printStackTrace()
            return false
        }
    }


    /**
     *
     */
    private fun getMachineList(): Boolean
    {
        val model = MachineViewModel(application)
        val maxDate = model.getMaxDate()

        Log.d(TAG,"# private fun getMachineList(): Boolean")
        Log.d(TAG,"# maxDate: $maxDate")

        val list = this.api.machineGetAll(this.token, maxDate).execute()

        if (list.body().isNullOrEmpty()) {
            Log.d(TAG,"MACHINE LIST IS NULL")
            return false
        }

        list.body()!!.forEach {
            model.save(it)
        }

        Log.d(TAG,"MACHINE LIST SAVED: "+ list.body()!!.size)
        this.logMessage("MACHINES RECEIVED: "+ list.body()!!.size)
        return true
    }


    /**
     *
     */
    private fun getUserList(): Boolean
    {
        val model = UserViewModel(application)
        val maxDate = model.getMaxDate()

        Log.d(TAG,"# private fun getUserList(): Boolean")
        Log.d(TAG,"# maxDate: $maxDate")

        val list = this.api.userGetAll(this.token, maxDate).execute()

        if (list.body().isNullOrEmpty()) {
            Log.d(TAG,"USER LIST IS NULL")
            return false
        }

        list.body()!!.forEach {
            model.save(it)
        }

        this.logMessage("USERS RECEIVED: "+ list.body()!!.size)
        return true
    }


    /**
     *
     */
    private fun getDriverList(): Boolean
    {
        val model   = DriverViewModel(application)
        val maxDate = model.getMaxDate()

        Log.d(TAG,"# private fun getDriverList(): Boolean")
        Log.d(TAG,"# maxDate: $maxDate")

        val list = this.api.driverGetAll(this.token, maxDate).execute()

        if (list.body().isNullOrEmpty()) {
            Log.d(TAG,"DRIVER LIST IS NULL")
            return false
        }

        list.body()!!.forEach {
            model.save(it)
        }

        this.logMessage("DRIVER RECEIVED: "+ list.body()!!.size)
        return true
    }



    /**
     *
     */
    private fun getActivityTypeList(): Boolean
    {
        val model   = MachineActivityTypeViewModel(application)
        val maxDate = model.getMaxDate()

        Log.d(TAG,"# private fun getActivityTypeList(): Boolean")
        Log.d(TAG,"# maxDate: $maxDate")

        val list = this.api.activityTypeGetAll(this.token, maxDate).execute()

        if (list.body().isNullOrEmpty()) {
            Log.d(TAG,"ACTIVITY LIST IS NULL")
            return false
        }

        list.body()!!.forEach {
            model.save(it)
        }

        this.logMessage("ACTIVITY RECEIVED: "+ list.body()!!.size)
        return true
    }


    /**
     *
     */
    private fun getEquipmentList(): Boolean
    {
        val model   = MachineEquipmentViewModel(application)
        val maxDate = model.getMaxDate()

        Log.d(TAG,"# private fun getEquipmentList(): Boolean")
        Log.d(TAG,"# maxDate: $maxDate")

        val list = this.api.equipmentGetAll(this.token, maxDate).execute()

        if (list.body().isNullOrEmpty()) {
            Log.d(TAG,"EQUIPMENT LIST IS NULL")
            return false
        }

        list.body()!!.forEach {
            model.save(it)
        }

        this.logMessage("EQUIPMENT RECEIVED: "+ list.body()!!.size)
        return true
    }


    /**
     *
     */
    private fun getMechanizationTypeList(): Boolean
    {
        val model   = MachineMechanizationTypeViewModel(application)
        val maxDate = model.getMaxDate()

        Log.d(TAG,"# private fun getMechanizationTypeList(): Boolean")
        Log.d(TAG,"# maxDate: $maxDate")

        val list = this.api.mechanizationTypeGetAll(this.token, maxDate).execute()

        if (list.body().isNullOrEmpty()) {
            Log.d(TAG,"MECHANIZATION TYPE LIST IS NULL")
            return false
        }

        list.body()!!.forEach {
            model.save(it)
        }

        this.logMessage("MECHANIZATION TYPE RECEIVED: "+ list.body()!!.size)
        return true
    }


    /**
     *
     */
    private fun getChecklistItemsList(): Boolean
    {
        val model   = ChecklistViewModel(application)
        val maxDate = model.getMaxDate()

        Log.d(TAG,"# private fun getChecklistItemsList(): Boolean")
        Log.d(TAG,"# maxDate: $maxDate")

        val list = this.api.checklistItemsGetAll(this.token, maxDate).execute()

        if (list.body().isNullOrEmpty()) {
            Log.d(TAG,"CHECKLIST ITEMS LIST IS NULL")
            return false
        }

        list.body()!!.forEach {
            model.save(it)
        }

        this.logMessage("CHECKLIST ITEMS RECEIVED: "+ list.body()!!.size)
        return true
    }


    /**
     *
     */
    private fun getFarmSectorList(): Boolean
    {
        val model   = FarmSectorViewModel(application)
        val maxDate = model.getMaxDate()

        Log.d(TAG,"# private fun getFarmSectorList(): Boolean")
        Log.d(TAG,"# maxDate: $maxDate")

        val list = this.api.farmSectorGetAll(this.token, maxDate).execute()

        if (list.body().isNullOrEmpty()) {
            Log.d(TAG,"FARM SECTOR LIST IS NULL")
            return false
        }

        list.body()!!.forEach {
            model.save(it)
        }

        this.logMessage("FARM SECTOR RECEIVED: "+ list.body()!!.size)
        return true
    }


    /**
     *
     */
    private fun getFarmCompartmentList(): Boolean
    {
        val model   = FarmCompartmentViewModel(application)
        val maxDate = model.getMaxDate()

        Log.d(TAG,"# private fun getFarmCompartmentList(): Boolean")
        Log.d(TAG,"# maxDate: $maxDate")

        val list = this.api.farmCompartmentGetAll(this.token, maxDate).execute()

        if (list.body().isNullOrEmpty()) {
            Log.d(TAG,"FARM COMPARTMENT LIST IS NULL")
            return false
        }

        list.body()!!.forEach {
            model.save(it)
        }

        this.logMessage("FARM COMPARTMENT RECEIVED: "+ list.body()!!.size)
        return true
    }


    /**
     *
     */
    private fun getAppointmentGroupList(): Boolean
    {
        val model   = AppointmentGroupViewModel(application)
        val maxDate = model.getMaxDate()

        Log.d(TAG,"# private fun getAppointmentGroupList(): Boolean")
        Log.d(TAG,"# maxDate: $maxDate")

        val list = this.api.appointmentGroupGetAll(this.token, maxDate).execute()

        if (list.body().isNullOrEmpty()) {
            Log.d(TAG,"APPOINTMENT GROUP LIST IS NULL")
            return false
        }

        list.body()!!.forEach {
            model.save(it)
        }

        this.logMessage("APPOINTMENT GROUP RECEIVED: "+ list.body()!!.size)
        return true
    }


    /**
     *
     */
    private fun getAppointmentList(): Boolean
    {
        val model   = AppointmentViewModel(application)
        val maxDate = model.getMaxDate()

        Log.d(TAG,"# private fun getAppointmentList(): Boolean")
        Log.d(TAG,"# maxDate: $maxDate")

        val list = this.api.appointmentListGetAll(this.token, maxDate).execute()

        if (list.body().isNullOrEmpty()) {
            Log.d(TAG,"APPOINTMENT LIST IS NULL")
            return false
        }

        list.body()!!.forEach {
            model.save(it)
        }

        this.logMessage("APPOINTMENT RECEIVED: "+ list.body()!!.size)
        return true
    }


    /**
     *
     */
    private fun getAppointmentMachineTypeList(): Boolean
    {
        val model   = AppointmentMachineTypeViewModel(application)
        val maxDate = model.getMaxDate()

        Log.d(TAG,"# private fun getAppointmentMachineTypeList(): Boolean")
        Log.d(TAG,"# maxDate: $maxDate")

        val list = this.api.appointmentMachineTypeGetAll(this.token, maxDate).execute()

        if (list.body().isNullOrEmpty()) {
            Log.d(TAG,"APPOINTMENT MACHINE TYPE LIST IS NULL")
            return false
        }

        list.body()!!.forEach {
            model.save(it)
        }

        this.logMessage("APPOINTMENT MACHINE TYPE RECEIVED: "+ list.body()!!.size)
        return true
    }


    /**
     *
     */
    private fun getAppointmentDataList(): Boolean
    {
        val model   = AppointmentDataViewModel(application)
        val maxDate = model.getMaxDate()

        Log.d(TAG,"# private fun getAppointmentDataList(): Boolean")
        Log.d(TAG,"# maxDate: $maxDate")

        val list = this.api.appointmentDataGetAll(this.token, maxDate).execute()

        if (list.body().isNullOrEmpty()) {
            Log.d(TAG,"APPOINTMENT DATA LIST IS NULL")
            return false
        }

        list.body()!!.forEach {
            model.save(it)
        }

        this.logMessage("APPOINTMENT DATA RECEIVED: "+ list.body()!!.size)
        return true
    }


    /**
     *
     */
    private fun getTelemetryMonitoringList(): Boolean
    {
        val model   = TelemetryMonitoringViewModel(application)
        val maxDate = model.getMaxDate()

        Log.d(TAG,"# private fun TelemetryMonitoringViewModel(): Boolean")
        Log.d(TAG,"# maxDate: $maxDate")

        val list = this.api.telemetryMonitoringGetAll(this.token, maxDate).execute()

        if (list.body().isNullOrEmpty()) {
            Log.d(TAG,"TELEMETRY MONITORING LIST IS NULL")
            return false
        }

        list.body()!!.forEach {
            model.save(it)
        }

        this.logMessage("TELEMETRY MONITORING RECEIVED: "+ list.body()!!.size)
        return true
    }


    /**
     *
     */
    private fun getMachineGroupList(): Boolean
    {
        val model   = MachineGroupViewModel(application)
        val maxDate = model.getMaxDate()

        Log.d(TAG,"# private fun getMachineGroupList(): Boolean")
        Log.d(TAG,"# maxDate: $maxDate")

        val list = this.api.machineGroupGetAll(this.token, maxDate).execute()

        if (list.body().isNullOrEmpty()) {
            Log.d(TAG,"MACHINE GROUP LIST IS NULL")
            return false
        }

        list.body()!!.forEach {
            model.save(it)
        }

        this.logMessage("MACHINE GROUP RECEIVED: "+ list.body()!!.size)
        return true
    }


    /**
     *
     */
    private fun sendAppointments() : Boolean
    {
        val endPoint = "appointment-saved/addList/"
        val model = AppointmentSavedViewModel(application)
        var total = 0
        do {
            val list = model.getAllNotSent()
            Log.d(TAG, "sendAppointments: "+ list.size)
            if (list.isNotEmpty()) {
                if (!this.synchronousPost(endPoint, Gson().toJson(list))) {
                    return false
                }
                list.forEach {
                    it.sentToCloud = true
                    model.save(it)
                    total++
                }
            }
        }
        while (list.isNotEmpty())
        this.logMessage("SENT APPOINTMENTS: $total")
        return true
    }


    /**
     *
     */
    private fun sendAppointmentSemiMechanized() : Boolean
    {
        val endPoint = "appointment-semi-mechanized/addList/"
        val model = AppointmentSemiMechanizedViewModel(application)
        var total = 0
        do {
            val list = model.getAllNotSent()
            Log.d(TAG, "sendAppointmentSemiMechanized: "+ list.size)
            if (list.isNotEmpty()) {
                if (!this.synchronousPost(endPoint, Gson().toJson(list))) {
                    return false
                }
                list.forEach {
                    it.sentToCloud = true
                    model.save(it)
                    total++
                }
            }
        }
        while (list.isNotEmpty())
        this.logMessage("SENT MANUAL APPOINTMENTS: $total")
        return true
    }


    /**
     *
     */
    private fun sendChecklistSaved() : Boolean
    {
        val endPoint = "checklist-saved/addList/"
        val model   = ChecklistSavedViewModel(application)
        var total   = 0
        do {
            val list = model.getAllNotSent()
            Log.d(TAG, "sendChecklistSaved: "+ list.size)
            if (list.isNotEmpty()) {
                if (!this.synchronousPost(endPoint, Gson().toJson(list))) {
                    return false
                }
                list.forEach {
                    it.sentToCloud = true
                    model.save(it)
                    total++
                }
            }
        }
        while (list.isNotEmpty())
        this.logMessage("SENT CHECKLIST: $total")
        return true
    }


    /**
     *
     */
    private fun sendDriverLogin() : Boolean
    {
        val endPoint = "driver-login/addList/"
        val model = DriverLoginViewModel(application)
        var total = 0
        do {
            val list = model.getAllNotSent()
            Log.d(TAG, "sendDriverLogin: "+ list.size)
            if (list.isNotEmpty()) {
                if (!this.synchronousPost(endPoint, Gson().toJson(list))) {
                    return false
                }
                list.forEach {
                    it.sentToCloud = true
                    model.save(it)
                    total++
                }
            }
        }
        while (list.isNotEmpty())
        this.logMessage("SENT LOGIN: $total")
        return true
    }


    /**
     *
     */
    private fun sendMachineSetup() : Boolean
    {
        val endPoint = "machine-setup/addList/"
        val model = MachineSetupViewModel(application)
        var total = 0
        do {
            val list = model.getAllNotSent()
            Log.d(TAG, "sendMachineSetup: "+ list.size)
            if (list.isNotEmpty()) {
                if (!this.synchronousPost(endPoint, Gson().toJson(list))) {
                    return false
                }
                list.forEach {
                    it.sentToCloud = true
                    model.save(it)
                    total++
                }
            }
        }
        while (list.isNotEmpty())
        this.logMessage("SENT SETUP: $total")
        return true
    }


    /**
     *
     */
    private fun sendProductionHarvester() : Boolean
    {
        val endPoint = "production-harvester/addList/"
        val model = ProductionHarvesterViewModel(application)
        var total = 0
        do {
            val list = model.getAllNotSent()
            Log.d(TAG, "sendProductionHarvester: "+ list.size)
            if (list.isNotEmpty()) {
                if (!this.synchronousPost(endPoint, Gson().toJson(list))) {
                    return false
                }
                list.forEach {
                    it.sentToCloud = true
                    model.save(it)
                    total++
                }
            }
        }
        while (list.isNotEmpty())
        this.logMessage("SENT PRODUCTION: $total")
        return true
    }


    /**
     *
     */
    private fun sendTelemetry() : Boolean
    {
        val endPoint = "telemetry-list/addList/"
        val model = TelemetryViewModel(application)
        var total = 0
        do {
            val list = model.getAllNotSent()
            Log.d(TAG, "sendTelemetry: "+ list.size)
            if (list.isNotEmpty()) {
                if (!this.synchronousPost(endPoint, Gson().toJson(list))) {
                    return false
                }
                list.forEach {
                    it.sentToCloud = true
                    model.save(it)
                    total++
                }
            }
        }
        while (list.isNotEmpty())
        this.logMessage("SENT TELEMETRY: $total")
        return true
    }


    /**
     *
     */
    private fun sendTelemetryAlert() : Boolean
    {
        val endPoint = "telemetry-alert/addList/"
        val model = TelemetryAlertViewModel(application)
        var total = 0
        do {
            val list = model.getAllNotSent()
            Log.d(TAG, "sendTelemetryAlert: "+ list.size)
            if (list.isNotEmpty()) {
                if (!this.synchronousPost(endPoint, Gson().toJson(list))) {
                    return false
                }
                list.forEach {
                    it.sentToCloud = true
                    model.save(it)
                    total++
                }
            }
        }
        while (list.isNotEmpty())
        this.logMessage("SENT TELEMETRY ALERT: $total")
        return true
    }


    /**
     *
     */
    private fun synchronousPost(endpoint : String, jsonBody: String ): Boolean
    {
        Log.d(TAG, "synchronousPost($endpoint)")
        val url = URL(createBaseUrl + endpoint)
        (url.openConnection() as? HttpURLConnection)?.run {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/json; utf-8")
            setRequestProperty("Accept", "application/json")
            setRequestProperty("Authorization", token)
            connectTimeout = 15000
            doOutput = true

            return try {
                outputStream.write(jsonBody.toByteArray())
                val content = BufferedReader(inputStream.reader()).readText()
                content == "true"
            } catch (e : Exception) {
                false
            }
        }
        return false
    }


    /**
     *
     */
    private fun logMessage(message:String)
    {
        MessageSingleton.messages.add(message +"\n")
    }


    /**
     *
     */
    interface ApiInterface {

        @POST("auth/login")
        fun login(
            @Body userRequest: UserRequestDto
        ): Call<AuthResponseDto>

        @GET("machine/getAllFrom/{maxDate}")
        fun machineGetAll(
            @Header("Authorization") authorization: String,
            @Path("maxDate") maxDate: String
        ): Call<List<Machine>>

        @GET("user/getAllFrom/{maxDate}")
        fun userGetAll(
            @Header("Authorization") authorization: String,
            @Path("maxDate") maxDate: String
        ): Call<List<User>>

        @GET("driver-list/getAllFrom/{maxDate}")
        fun driverGetAll(
            @Header("Authorization") authorization: String,
            @Path("maxDate") maxDate: String
        ): Call<List<Driver>>

        @GET("machine-activity-type/getAllFrom/{maxDate}")
        fun activityTypeGetAll(
            @Header("Authorization") authorization: String,
            @Path("maxDate") maxDate: String
        ): Call<List<MachineActivityType>>

        @GET("machine-equipment/getAllFrom/{maxDate}")
        fun equipmentGetAll(
            @Header("Authorization") authorization: String,
            @Path("maxDate") maxDate: String
        ): Call<List<MachineEquipment>>

        @GET("machine-mechanization-type/getAllFrom/{maxDate}")
        fun mechanizationTypeGetAll(
            @Header("Authorization") authorization: String,
            @Path("maxDate") maxDate: String
        ): Call<List<MachineMechanizationType>>

        @GET("checklist-items/getAllFrom/{maxDate}")
        fun checklistItemsGetAll(
            @Header("Authorization") authorization: String,
            @Path("maxDate") maxDate: String
        ): Call<List<Checklist>>

        @GET("farm-sector/getAllFrom/{maxDate}")
        fun farmSectorGetAll(
            @Header("Authorization") authorization: String,
            @Path("maxDate") maxDate: String
        ): Call<List<FarmSector>>

        @GET("farm-compartment/getAllFrom/{maxDate}")
        fun farmCompartmentGetAll(
            @Header("Authorization") authorization: String,
            @Path("maxDate") maxDate: String
        ): Call<List<FarmCompartment>>

        @GET("appointment-group/getAllFrom/{maxDate}")
        fun appointmentGroupGetAll(
            @Header("Authorization") authorization: String,
            @Path("maxDate") maxDate: String
        ): Call<List<AppointmentGroup>>

        @GET("appointment-list/getAllFrom/{maxDate}")
        fun appointmentListGetAll(
            @Header("Authorization") authorization: String,
            @Path("maxDate") maxDate: String
        ): Call<List<Appointment>>

        @GET("appointment-machine-type/getAllFrom/{maxDate}")
        fun appointmentMachineTypeGetAll(
            @Header("Authorization") authorization: String,
            @Path("maxDate") maxDate: String
        ): Call<List<AppointmentMachineType>>

        @GET("appointment-data/getAllFrom/{maxDate}")
        fun appointmentDataGetAll(
            @Header("Authorization") authorization: String,
            @Path("maxDate") maxDate: String
        ): Call<List<AppointmentData>>

        @GET("telemetry-monitoring/getAllFrom/{maxDate}")
        fun telemetryMonitoringGetAll(
            @Header("Authorization") authorization: String,
            @Path("maxDate") maxDate: String
        ): Call<List<TelemetryMonitoring>>

        @GET("machine-group/getAllFrom/{maxDate}")
        fun machineGroupGetAll(
            @Header("Authorization") authorization: String,
            @Path("maxDate") maxDate: String
        ): Call<List<MachineGroup>>
    }

}
