package br.com.crearesistemas.socket.client.service


import android.R.attr.host
import android.R.attr.port
import android.app.Application
import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import br.com.crearesistemas.shift_leader.db_service.model.*
import br.com.crearesistemas.shift_leader.db_service.viewmodel.*
import com.google.gson.Gson
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress
import java.nio.charset.Charset
import java.util.*


/**
 *      CREARE SISTEMAS 2022
 *
 *      @author: Luiz Gregory
 *      @email: luiz.gregory@crearesistemas.com.br
 *
 */
open class ClientSocketController(val application: Application)
{
    /**
     *
     */
    private var runThread: RunThread? = null

    private lateinit var appointmentsService: AppointmentViewModel
    private lateinit var appointmentsGroupService: AppointmentGroupViewModel
    private lateinit var checklistService: ChecklistViewModel
    private lateinit var driverService: DriverViewModel
    private lateinit var mechanizationService: MachineMechanizationTypeViewModel
    private lateinit var farmSectorsService: FarmSectorViewModel
    private lateinit var farmCompartmentService: FarmCompartmentViewModel
    private lateinit var activityTypeService: MachineActivityTypeViewModel
    private lateinit var equipmentService: MachineEquipmentViewModel
    private lateinit var appointmentDataService: AppointmentDataViewModel
    private lateinit var appointmentMachineTypeService: AppointmentMachineTypeViewModel
    private lateinit var maintenanceTaskService: MaintenanceTaskViewModel
    private lateinit var machineGroupService: MachineGroupViewModel
    private lateinit var telemetryMonitoringModel: TelemetryMonitoringViewModel
    private lateinit var telemetryModel: TelemetryViewModel
    private lateinit var telemetryAlertModel: TelemetryAlertViewModel
    private lateinit var machineSetupModel: MachineSetupViewModel
    private lateinit var machineChecklistModel: ChecklistSavedViewModel
    private lateinit var machineAppoinmentModel: AppointmentSavedViewModel
    private lateinit var driverLoginViewModel: DriverLoginViewModel
    private lateinit var productionHarvesterViewModel: ProductionHarvesterViewModel

    val PORT = 6589
    val SERVER_ADDR = "10.0.0.5"
    //val SERVER_ADDR = "192.168.43.218"

    /**
     *
     */
    companion object
    {
        private val TAG = ClientSocketController::class.java.simpleName
    }


    /**
     *
     */
    fun start()
    {
        if (this.runThread != null) {
            return
        }

        this.appointmentsService            = AppointmentViewModel(application)
        this.appointmentsGroupService       = AppointmentGroupViewModel(application)
        this.checklistService               = ChecklistViewModel(application)
        this.driverService                  = DriverViewModel(application)
        this.mechanizationService           = MachineMechanizationTypeViewModel(application)
        this.farmSectorsService             = FarmSectorViewModel(application)
        this.farmCompartmentService         = FarmCompartmentViewModel(application)
        this.activityTypeService            = MachineActivityTypeViewModel(application)
        this.equipmentService               = MachineEquipmentViewModel(application)
        this.appointmentDataService         = AppointmentDataViewModel(application)
        this.appointmentMachineTypeService  = AppointmentMachineTypeViewModel(application)
        this.maintenanceTaskService         = MaintenanceTaskViewModel(application)
        this.telemetryMonitoringModel       = TelemetryMonitoringViewModel(application)
        this.telemetryModel                 = TelemetryViewModel(application)
        this.telemetryAlertModel            = TelemetryAlertViewModel(application)
        this.machineSetupModel              = MachineSetupViewModel(application)
        this.machineChecklistModel          = ChecklistSavedViewModel(application)
        this.machineAppoinmentModel         = AppointmentSavedViewModel(application)
        this.driverLoginViewModel           = DriverLoginViewModel(application)
        this.productionHarvesterViewModel   = ProductionHarvesterViewModel(application)
	    this.machineGroupService            = MachineGroupViewModel(application)

        Log.d(TAG,"connect()::#################################################################################################################")
        Log.d(TAG, "connect()::PORT:: $SERVER_ADDR:$PORT")

        synchronized(this) {
            this.runThread?.interrupted = true
            this.runThread?.interrupt()
            this.runThread = null
            this.runThread = RunThread()
            this.runThread?.start()
        }
    }


    /**
     *
     */
    fun onDestroy()
    {
        Log.d(TAG, "disconnect()")
        synchronized(this) {
            this.runThread?.interrupted = true
            this.runThread?.interrupt()
            this.runThread = null
        }
    }


    /**
     *
     */
    protected inner class RunThread : Thread()
    {
        var interrupted: Boolean = false
        private var connection: Socket? = null
        private var reader: Scanner? = null
        private var writer: OutputStream? = null
        private var readContent: String = ""
        private var timeout: Int = 3
        private var frameArray: List<String>? = null
        private var code: Long? = 0
        private var command: String? = ""
        private var content: String? = ""
        private var attempts : Int = 1

        /**
         * Método principal
         */
        override fun run()
        {
            Log.d(TAG, "# SOCKET RUN ")

            while(!interrupted) {

                Log.d(TAG, "# SOCKET CONNECTION : $interrupted $attempts")
                if (!this.socketConnect()) {
                    continue
                }

                MachineStatusSingleton.messagesSocket.clear()
                MachineStatusSingleton.messagesSocket.add("CONNECTION SUCCESS!! \n")

                val startDate = Date()
                Log.d(TAG,"###########################################################################################################################")
                Log.d(TAG, "# SOCKET RECEIVE DATA : START ")
                if(!this.getList("getLogin",            driverLoginViewModel.getMaxDate( this.getCurrentWiFiSSID()!! ))) continue
                if(!this.getList("getTelemetry",        telemetryModel.getMaxDate( this.getCurrentWiFiSSID()!! ))) continue
                if(!this.getList("getTelemetryAlert",   telemetryAlertModel.getMaxDate( this.getCurrentWiFiSSID()!! ))) continue
                if(!this.getList("getSetup",            machineSetupModel.getMaxDate( this.getCurrentWiFiSSID()!! ))) continue
                if(!this.getList("getChecklist",        machineChecklistModel.getMaxDate( this.getCurrentWiFiSSID()!! ))) continue
                if(!this.getList("getAppointment",      machineAppoinmentModel.getMaxDate( this.getCurrentWiFiSSID()!! ))) continue
                if(!this.getList("getProduction",       productionHarvesterViewModel.getMaxDate( this.getCurrentWiFiSSID()!! ))) continue
                Log.d(TAG, "# SOCKET RECEIVE DATA : FINAL ")

                Log.d(TAG,"###########################################################################################################################")
                Log.d(TAG, "# SOCKET SEND DATA : START ")

                val driverList = driverService.getAllFrom( this.getMaxDate("driver") )
                if(!this.sendListItem("saveDriver", driverList)) continue
                if(driverList.isNotEmpty()) MachineStatusSingleton.messagesSocket.add("SENDING DRIVER ${driverList.size}. \n")

                val mechanizationList = mechanizationService.getAllFrom( this.getMaxDate("mechanizationType") )
                if(!this.sendListItem("saveMechanizations", mechanizationList)) continue
                if(mechanizationList.isNotEmpty()) MachineStatusSingleton.messagesSocket.add("SENDING MECHANIZATION ${mechanizationList.size}. \n")

                val activityTypeList = activityTypeService.getAllFrom( this.getMaxDate("activityType") )
                if(!this.sendListItem("saveActivityType", activityTypeList)) continue
                if(activityTypeList.isNotEmpty()) MachineStatusSingleton.messagesSocket.add("SENDING ACTIVITY TYPE ${activityTypeList.size}. \n")

                val equipmentList = equipmentService.getAllFrom( this.getMaxDate("equipmentType") )
                if(!this.sendListItem("saveEquipment", equipmentList)) continue
                if(equipmentList.isNotEmpty()) MachineStatusSingleton.messagesSocket.add("SENDING EQUIPMENTS ${equipmentList.size}. \n")

                val checklistList = checklistService.getAllFrom( this.getMaxDate("checklistItems") )
                if(!this.sendListItem("saveChecklist", checklistList)) continue
                if(checklistList.isNotEmpty()) MachineStatusSingleton.messagesSocket.add("SENDING CHECKLISTS ${checklistList.size}. \n")

                val appointmentsGroupList = appointmentsGroupService.getAllFrom( this.getMaxDate("appointmentsGroup") )
                if(!this.sendListItem("saveAppointmentsGroup", appointmentsGroupList)) continue
                if(appointmentsGroupList.isNotEmpty()) MachineStatusSingleton.messagesSocket.add("SENDING APPOINTMENTS GROUP ${appointmentsGroupList.size}. \n")

                val appointmentDataList = appointmentDataService.getAllFrom( this.getMaxDate("appointmentData") )
                if(!this.sendListItem("saveAppointmentData", appointmentDataList)) continue
                if(appointmentDataList.isNotEmpty()) MachineStatusSingleton.messagesSocket.add("SENDING APPOINTMENTS DATA ${appointmentDataList.size}. \n")

                val appointmentMachineTypeList = appointmentMachineTypeService.getAllFrom( this.getMaxDate("appointmentMachineType") )
                if(!this.sendListItem("saveAppointmentMachineType", appointmentMachineTypeList)) continue
                if(appointmentMachineTypeList.isNotEmpty()) MachineStatusSingleton.messagesSocket.add("SENDING APPOINTMENTS MACHINE TYPE ${appointmentMachineTypeList.size}. \n")

                val appointmentList = appointmentsService.getAllFrom( this.getMaxDate("appointmentList") )
                if(!this.sendListItem("saveAppointments", appointmentList)) continue
                if(appointmentList.isNotEmpty())  MachineStatusSingleton.messagesSocket.add("SENDING APPOINTMENTS ${appointmentList.size}. \n")

                val farmSectorList = farmSectorsService.getAllFrom( this.getMaxDate("farmSector") )
                if(!this.sendListItem("saveFarmSectors", farmSectorList)) continue
                if(farmSectorList.isNotEmpty()) MachineStatusSingleton.messagesSocket.add("SENDING FARM SECTORS ${farmSectorList.size}. \n")

                val farmCompartmentList = farmCompartmentService.getAllFrom( this.getMaxDate("farmCompartment") )
                if(!this.sendListItem("saveFarmCompartment", farmCompartmentList )) continue
                if(farmCompartmentList.isNotEmpty()) MachineStatusSingleton.messagesSocket.add("SENDING FARM COMPARTMENTS ${farmCompartmentList.size}. \n")

                val telemetryMonitoring = telemetryMonitoringModel.getAllFrom( this.getMaxDate("telemetryMonitoring") )
                if(!this.sendListItem("saveTelemetryMonitoring", telemetryMonitoring )) continue
                if(telemetryMonitoring.isNotEmpty()) MachineStatusSingleton.messagesSocket.add("SENDING TELEMETRY MONITORING ${telemetryMonitoring.size}. \n")

                val machineGroupList = machineGroupService.getAllFrom( this.getMaxDate("machineGroup") )
                if(!this.sendListItem("saveMachineGroup", machineGroupList )) continue
                if(machineGroupList.isNotEmpty()) MachineStatusSingleton.messagesSocket.add("SENDING MACHINE GROUP ${telemetryMonitoring.size}. \n")

                Log.d(TAG, "# SOCKET SEND DATA : FINAL ")
                val diffTime = this.differenceInSeconds(startDate, Date())

                MachineStatusSingleton.messagesSocket.add("ALL DATA SYNCHRONIZED!! \n")
                MachineStatusSingleton.messagesSocket.add("FINISHED SUCCESSFUL \n")
                MachineStatusSingleton.messagesSocket.add("TOTAL TIME: $diffTime SECONDS \n")

                Log.d(TAG,"###########################################################################################################################")
                this.socketDisconnect()
            }
        }


        /**
         *
         */
        private fun sendListItem(method: String, list: List<*>): Boolean
        {
            var total = list.size
            Log.d(TAG, "sendListItem: $method : $total")
            if (total == 0) {
                this.sendFinish(total.toLong())
                return this.waitingAck(total.toLong())
            }

            var attempts = 0
            var i = 0
            while (i < total) {
                var jItem = Gson().toJson(list[i])
                this.write("CS|$method|$i|$jItem|")
                if (!this.waitingAck(i.toLong())) {
                    if (attempts++ >= 3) return false
                    continue
                }
                i++
            }

            this.sendFinish(total.toLong())
            if (!this.waitingAck(total.toLong())) {
                return false
            }
            return true
        }


        /**
         *
         */
        private fun getList(method: String, maxDate: String): Boolean
        {
            MachineStatusSingleton.messagesSocket.add("RECEIVED: $method; ")

            var i = (0..100).random()
            this.write("CS|$method|$i|$maxDate|")
            if (!this.waitingAck(i.toLong())) {
                return false
            }
            var t = 0;

            while (!this.waitingCommand().isNullOrEmpty()) {
                try {
                    when (this.command!!) {
                         "saveLogin"            -> driverLoginViewModel.save( Gson().fromJson( this.content!!, DriverLogin::class.java ) )
                         "saveTelemetry"        -> telemetryModel.save( Gson().fromJson( this.content!!, Telemetry::class.java ) )
                         "saveTelemetryAlert"   -> telemetryAlertModel.save( Gson().fromJson( this.content!!, TelemetryAlert::class.java ) )
                         "saveSetup"            -> machineSetupModel.save( Gson().fromJson( this.content!!, MachineSetup::class.java ) )
                         "saveChecklist"        -> machineChecklistModel.save( Gson().fromJson( this.content!!, ChecklistSaved::class.java ) )
                         "saveAppointment"      -> machineAppoinmentModel.save( Gson().fromJson( this.content!!, AppointmentSaved::class.java ) )
                         "saveProduction"       -> productionHarvesterViewModel.save( Gson().fromJson( this.content!!, ProductionHarvester::class.java ) )
                         "finish"               -> {
                             MachineStatusSingleton.messagesSocket.add("SUCCESS! TOTAL: $t \n")
                             return true
                         }
                    }
                    t++
                    //Log.d(TAG, "${this.command}: SUCESS ")
                }
                catch (e: Exception) {
                    e.printStackTrace()
                    Log.d(TAG, "${this.command}: Exception ")
                    return false
                }
            }

            return true
        }



        /**
         *
         */
        private fun getMaxDate(method: String): String
        {
            var i = (0..100).random()
            this.write("CS|getMaxDate|$i|$method|")
            if (!this.waitingAck(i.toLong())) {
                return "2022-01-01 00:00:00+00:00"
            }

            while (!this.waitingCommand().isNullOrEmpty()) {
                if(this.command!! == "maxDate") {
                    return this.content!!
                }
            }

            return "2022-01-01 00:00:00+00:00"
        }



        /**
         *
         */
        private fun waitingCommand(): String?
        {
            var content = this.readLine( 3 )
            if (content.isNullOrEmpty()) {
                return null
            }
            if (!this.checkFrame(content)) {
                this.sendNack(0, "InvalidFrame")
                return null
            }
            if (!this.checkCRC(content)) {
                this.sendNack(0, "InvalidCRC")
                return null
            }

            this.code = this.frameArray!![2].toLong()
            this.command = this.frameArray!![1]
            this.content = this.frameArray!![3]

            if (this.command in arrayOf("ACK", "NACK")) {
                return null
            }

            this.sendAck(this.code!!, this.command!!)
            return this.command
        }


        /**
         *  Valida de a estrutura basica do frame é válida
         */
        private fun checkFrame(frame: String): Boolean
        {
            val pattern = Regex("^CS\\|[a-zA-Z0-9\\-]{1,30}\\|.*\\|[0-9]{1,6}\$")
            val ans: MatchResult? = pattern.find(frame)
            if (ans?.value.isNullOrEmpty()) {
                Log.d(TAG, "INVALID FRAME: $frame")
                return false
            }
            return true
        }


        /**
         *
         */
        private fun sendAck(code: Long, command: String)
        {
            this.write("CS|ACK|$code|$command|")
        }


        /**
         *
         */
        private fun sendFinish(code: Long)
        {
            this.write("CS|finish|$code|")
        }

        /**
         *
         */
        private fun sendNack(code: Long, reason: String)
        {
            this.write("CS|NACK|$code|$reason|")
        }


        /**
         *
         */
        private fun write(message: String)
        {
            try {
                val crc = this.getCRC(message)
                val complete = "$message$crc"
                this.writer?.write("$complete\n".toByteArray(Charset.defaultCharset()))
                Log.d(TAG, " -> write( $complete )")
            }
            catch (e: Exception) {
            }
        }


        /**
         * CRC-16/CCITT-FALSE
         */
        private fun getCRC(frame: String): Int {
            var crc = 0xFFFF
            var charArray = frame.toByteArray(Charsets.UTF_8)
            for (element in charArray) {
                var x = ((crc shr 8) xor element.toInt()) and 0xFF
                x = x xor (x shr 4)
                crc = ((crc shl 8) xor (x shl 12) xor (x shl 5) xor x) and 0xFFFF
            }
            return crc
        }


        /**
         *
         */
        private fun waitingAck(commandId: Long): Boolean
        {
            val ackFrame = this.readLine(1)
            if (ackFrame.isEmpty()) {
                this.forceReconnect()
                return false
            }
            if (!this.checkFrame(ackFrame)) {
                return false
            }
            if (!this.checkCRC(ackFrame)) {
                return false
            }
            if (this.frameArray!![1] !in arrayOf("ACK")) {
                return false
            }
            if (this.frameArray!![2].toLong() != commandId) {
                return false
            }

            return true
        }


        /**
         *
         */
        private fun checkCRC(frame: String): Boolean {
            this.frameArray = frame.split("|")
            val frameCRC = this.frameArray!!.last().trim().replace(".0", "")
            val pattern = Regex("[0-9.]{1,8}$")
            val nframe = frame.replace(pattern, "")
            val crcCalc = getCRC(nframe)
            return crcCalc == frameCRC.toInt()
        }


        /**
         *
         */
        private fun readLine( timeOut : Int? = null): String {

            var tout = if (timeOut == null) this.timeout else timeOut

            val startRead = Date()
            do {
                sleep(1)
            }
            while ((this.differenceInSeconds( startRead, Date() ) <= tout) && this.readContent.isEmpty() )

            if (!this.readContent.isNullOrEmpty()) {
                Log.d(TAG, " <- read: ${this.readContent}")
            }

            val res = this.readContent
            this.readContent = ""
            return res
        }


        /**
         *
         */
        inner class ReadThread : Thread()
        {
            override fun run() {
                while (true) {
                    try {
                        readContent = reader?.nextLine().toString()
                        sleep(2)
                    }
                    catch (e: Exception) {
                        if(reader == null) break
                    }
                }
                println("ReadThread : Thread() == STOP")
            }
        }


        /**
         *
         */
        private fun differenceInSeconds(startDate: Date, endDate: Date): Long {
            return (endDate.time - startDate.time) / 1000
        }


        /**
         *
         */
        private fun socketConnect(): Boolean
        {
            if (this.connection != null && this.connection!!.isConnected) return true

            try {
                Log.d(TAG, "############################################################################################")
                Log.d(TAG, "# Socket Connection: $SERVER_ADDR:$PORT")

                MachineStatusSingleton.messagesSocket.clear()
                MachineStatusSingleton.messagesSocket.add("!!! TRYING TO CONNECT !!! (${this.attempts}) \n")

                this.connection = Socket()
                this.connection!!.connect(InetSocketAddress(SERVER_ADDR, PORT), 10000)
                if (this.connection!!.isConnected) {
                    this.reader = Scanner(this.connection!!.getInputStream())
                    this.writer = this.connection!!.getOutputStream()
                    sleep(10)
                    ReadThread().start()
                    Log.d(TAG, "# Socket Connection: OK!! ")
                    this.attempts = 1
                    sleep(100)
                    return true
                }
            }
            catch (e: Exception) {
                Log.e(TAG, "Socket Connection Fail! this.attempts = ${this.attempts}")
            }

            this.forceReconnect()
            if(this.attempts++ >= 20) {
                MachineStatusSingleton.messagesSocket.clear()
                MachineStatusSingleton.messagesSocket.add("\n\tCONNECTION FAILED\n")
                this.socketDisconnect()
            }
            return false
        }


        /**
         *
         */
        private fun forceReconnect()
        {
            println("private fun forceReconnect() START")
            try { ReadThread().interrupt() } catch (e: Exception) { println(e.message) }
            try { this.connection?.shutdownInput() } catch (e: Exception) { println(e.message) }
            try { this.connection?.shutdownOutput() } catch (e: Exception) { println(e.message) }
            try { this.connection?.close() } catch (e: Exception) { println(e.message) }
            try { this.writer?.close() } catch (e: Exception) { println(e.message) }
            try { this.reader?.close() } catch (e: Exception) { println(e.message) }
            this.connection = null
            this.reader = null
            this.reader = null
            this.writer = null
            println("private fun forceReconnect() FINAL")
            sleep(1000)
        }



        /**
         *
         */
        private fun socketDisconnect() {
            try {
                this.write("CS|disconnect|0|")
                sleep(50)
                interrupted = true
                this.forceReconnect()
                onDestroy()
            }
            catch (e: Exception) {
            }
        }


        /**
         *
         */
        private fun getCurrentWiFiSSID(): String? {
            val wifiManager = application.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            return wifiManager.connectionInfo.ssid.replace("\"", "")
        }

    }

}
