package br.com.crearesistemas.shift_leader.db_service.repository

import android.app.Application
import android.os.AsyncTask
import br.com.crearesistemas.shift_leader.db_service.AppDatabase
import br.com.crearesistemas.shift_leader.db_service.model.MachineGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ExecutionException

class MachineGroupRepository (application: Application){

    private val db = AppDatabase.getInstance(application)
    private val dao = db!!.machineGroupDao()


    fun save(vararg values: MachineGroup) = runBlocking {
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

    fun getById(id: Long): MachineGroup? {

        try {
            return object : AsyncTask<Any?, Any?, Any?>() {
                override fun doInBackground(objects: Array<Any?>): Any? {
                    return dao.getById(id)
                }
            }.execute().get() as MachineGroup?
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return null
    }


    fun getAll(): List<MachineGroup>
    {
        try {
            return dao.getAll()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }


    fun getMaxDate(): String
    {
        try {
            var maxDate = dao.getMaxDate()
            if(!maxDate.isNullOrEmpty()) {
                return maxDate.replace("T", " ")
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return "2022-01-01 00:00:00.000-00:00"
    }


    fun getAllFrom(maxDate : String ): List<MachineGroup>
    {
        try {
            return dao.getAllFrom(maxDate)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

}
