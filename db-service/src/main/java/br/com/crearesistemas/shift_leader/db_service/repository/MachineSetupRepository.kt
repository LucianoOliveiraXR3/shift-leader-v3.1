package br.com.crearesistemas.shift_leader.db_service.repository
import android.app.Application
import android.os.AsyncTask
import br.com.crearesistemas.shift_leader.db_service.AppDatabase
import br.com.crearesistemas.shift_leader.db_service.model.MachineSetup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.*
import java.util.concurrent.ExecutionException
class MachineSetupRepository(application: Application) {

    private val db = AppDatabase.getInstance(application)
    private val dao = db!!.machineSetupDao()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())

    fun save(vararg values: MachineSetup) = runBlocking {
        values.forEach {
            it.collectedAt = dateFormat.format(Date())
        }
        dao.save(*values)
    }

    fun deleteById(id: Long, origin: String) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteById(id, origin)
        }
    }

    fun deleteAll(value: List<MachineSetup>) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAll(value)
        }
    }

    fun getById(id: Long, origin: String): MachineSetup? {

        try {
            return object : AsyncTask<Any?, Any?, Any?>() {
                override fun doInBackground(objects: Array<Any?>): Any? {
                    return dao.getById(id,origin)
                }
            }.execute().get() as MachineSetup?
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return null
    }


    fun getAll(): List<MachineSetup> {

        try {
            return object : AsyncTask<Any?, Any?, Any?>() {
                override fun doInBackground(objects: Array<Any?>): Any? {
                    return dao.getAll()
                }
            }.execute().get() as List<MachineSetup>
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return emptyList()
    }


    /**
     *
     */
    fun getAllNotSent(): List<MachineSetup>
    {
        try {
            return dao.getAllNotSent()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }



    fun getMax(origin: String): Long {

        try {
            return object : AsyncTask<Any?, Any?, Any?>() {
                override fun doInBackground(objects: Array<Any?>): Any? {
                    return dao.getMax(origin)
                }
            }.execute().get() as Long
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return 0L
    }



    fun getMaxDate(origin: String): String {
        try {
            var maxDate = dao.getMaxDate(origin)
            if(!maxDate.isNullOrEmpty()) {
                return maxDate
            }
        }
        catch (e: InterruptedException) {
            e.printStackTrace()
        }
        catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return "2022-01-01T00:00:00.000-0300"
    }


}