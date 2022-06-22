package br.com.crearesistemas.shift_leader.db_service.repository

import android.app.Application
import android.os.AsyncTask
import br.com.crearesistemas.shift_leader.db_service.AppDatabase
import br.com.crearesistemas.shift_leader.db_service.model.AppointmentMachineType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ExecutionException


class AppointmentMachineTypeRepository(application: Application) {

    private val db = AppDatabase.getInstance(application)
    private val dao = db!!.appointmentMachineType()

    fun save(vararg values: AppointmentMachineType) = runBlocking {
        dao.save(*values)
    }

    fun deleteById(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteById(id)
        }
    }

    fun deleteAll() = runBlocking {
        dao.deleteAll()
    }

    fun getById(id: Long): AppointmentMachineType? {
        try {
            return object : AsyncTask<Any?, Any?, Any?>() {
                override fun doInBackground(objects: Array<Any?>): Any? {
                    return dao.getById(id)
                }
            }.execute().get() as AppointmentMachineType?
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return null
    }

    fun getAll(): List<AppointmentMachineType> {
        try {
            return object : AsyncTask<Any?, Any?, Any?>() {
                override fun doInBackground(objects: Array<Any?>): Any? {
                    return dao.getAll()
                }
            }.execute().get() as List<AppointmentMachineType>
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return emptyList()
    }


    fun getAllFrom(maxDate : String ): List<AppointmentMachineType> {

        try {
            return dao.getAllFrom(maxDate)
        }
        catch (e: InterruptedException) {
            e.printStackTrace()
        }
        catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return emptyList()
    }


    fun getMaxDate(): String {
        try {
            var maxDate = dao.getMaxDate()
            if(!maxDate.isNullOrEmpty()) {
                return maxDate.replace("T", " ")
            }
        }
        catch (e: InterruptedException) {
            e.printStackTrace()
        }
        catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return "2022-01-01 00:00:00.000-00:00"
    }
}
