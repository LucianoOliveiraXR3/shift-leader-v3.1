package br.com.crearesistemas.shift_leader.db_service.repository

import android.app.Application
import android.os.AsyncTask
import br.com.crearesistemas.shift_leader.db_service.AppDatabase
import br.com.crearesistemas.shift_leader.db_service.model.TabletConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ExecutionException

class TabletConfigurationRepository(application: Application) {

    private val db = AppDatabase.getInstance(application)

    private val dao = db!!.tabletConfigurationDao()

    fun save(vararg values: TabletConfiguration) = runBlocking {
        dao.save(*values)
    }

    fun deleteById(key: String) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteById(key)
        }
    }

    fun deleteAll(value: List<TabletConfiguration>) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAll(value)
        }
    }

    fun getById(key: String): TabletConfiguration? {

        try {
            return object : AsyncTask<Any?, Any?, Any?>() {
                override fun doInBackground(objects: Array<Any?>): Any? {
                    return dao.getById(key)
                }
            }.execute().get() as TabletConfiguration?
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return null
    }

    fun getAll(): List<TabletConfiguration> {

        try {
            return object : AsyncTask<Any?, Any?, Any?>() {
                override fun doInBackground(objects: Array<Any?>): Any? {
                    return dao.getAll()
                }
            }.execute().get() as List<TabletConfiguration>
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return emptyList()
    }

}