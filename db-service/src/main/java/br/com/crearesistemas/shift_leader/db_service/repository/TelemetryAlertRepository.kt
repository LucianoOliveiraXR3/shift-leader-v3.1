package br.com.crearesistemas.shift_leader.db_service.repository
import android.app.Application
import android.os.AsyncTask
import br.com.crearesistemas.shift_leader.db_service.AppDatabase
import br.com.crearesistemas.shift_leader.db_service.model.TelemetryAlert
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.*
import java.util.concurrent.ExecutionException
class TelemetryAlertRepository(application: Application) {

    private val db = AppDatabase.getInstance(application)
    private val dao = db!!.telemetryAlertDao()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())

    fun save(vararg values: TelemetryAlert) = runBlocking {
        values.forEach {
            it.collectedAt = dateFormat.format(Date())
        }
        dao.save(*values)
    }

    fun deleteById(pgn: Long, eventTime: OffsetDateTime, origin: String) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteById(pgn, eventTime, origin)
        }
    }

    fun deleteAll(value: List<TelemetryAlert>) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAll(value)
        }
    }

    fun getById(pgn: Long, eventTime: OffsetDateTime, origin: String): TelemetryAlert? {

        try {
            return object : AsyncTask<Any?, Any?, Any?>() {
                override fun doInBackground(objects: Array<Any?>): Any? {
                    return dao.getById(pgn,eventTime,origin)
                }
            }.execute().get() as TelemetryAlert?
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return null
    }

    fun getAll(): List<TelemetryAlert> {

        try {
            return object : AsyncTask<Any?, Any?, Any?>() {
                override fun doInBackground(objects: Array<Any?>): Any? {
                    return dao.getAll()
                }
            }.execute().get() as List<TelemetryAlert>
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return emptyList()
    }



    fun getMaxDate(origin: String): String {
        try {
            var maxDate = dao.getMaxDate(origin)
            if(!maxDate.isNullOrEmpty()) {
                return maxDate
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return "2022-01-01T00:00:00.000-0300"
    }


    /**
     *
     */
    fun getAllNotSent(): List<TelemetryAlert> {

        try {
            return dao.getAllNotSent()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

}