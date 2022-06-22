package br.com.crearesistemas.shift_leader.db_service.repository

import android.app.Application
import android.os.AsyncTask
import br.com.crearesistemas.shift_leader.db_service.AppDatabase
import br.com.crearesistemas.shift_leader.db_service.model.MaintenanceTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ExecutionException

class MaintenanceTaskRepository(application: Application){

    private  val db = AppDatabase.getInstance(application)
    private val dao = db!!.maintenanceTaskDao()

    fun save(vararg values: MaintenanceTask) = runBlocking {
        dao.save(*values)
    }

    fun deleteById(id: Long){
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteById(id)
        }
    }

    fun deleteAll(value: List<MaintenanceTask>){
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAll(value)
        }
    }

    fun getById(id: Long): MaintenanceTask? {
        try {
            return object : AsyncTask<Any?, Any?, Any?>(){
                override fun doInBackground(objects: Array<Any?>): Any? {
                    return  dao.getById(id)
                }
            }.execute().get() as MaintenanceTask?
        }catch (e : InterruptedException){
            e.printStackTrace()
        }catch (e: ExecutionException){
            e.printStackTrace()
        }
        return null
    }

    fun getLastVersion(): Long? {
        try {
            return object : AsyncTask<Any?, Any?, Any?>(){
                override fun doInBackground(objects: Array<Any?>): Any? {
                    return  dao.getLatestVersion()
                }
            }.execute().get() as Long?
        }catch (e : InterruptedException){
            e.printStackTrace()
        }catch (e: ExecutionException){
            e.printStackTrace()
        }
        return null
    }

    fun getAll() : List<MaintenanceTask> {
        try {
            return object : AsyncTask<Any?, Any?, Any?>() {
                override fun doInBackground(objects: Array<Any?>): Any? {
                    return dao.getAll()
                }
            }.execute().get() as List<MaintenanceTask>
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return emptyList()
    }
}