package br.com.crearesistemas.shift_leader.db_service.repository
import android.app.Application
import android.os.AsyncTask
import br.com.crearesistemas.shift_leader.db_service.AppDatabase
import br.com.crearesistemas.shift_leader.db_service.model.Appointment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutionException


/**
 *
 */
class AppointmentRepository(application: Application) {

    private val db = AppDatabase.getInstance(application)
    private val dao = db!!.appointmentDao()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())

    fun save(vararg values: Appointment) = runBlocking {
        values.forEach {
            it.collectedAt = dateFormat.format(Date())
        }
        dao.save(*values)
    }

    fun saveAll(value: List<Appointment>) {
        CoroutineScope(Dispatchers.IO).launch {
            value.forEach {
                it.collectedAt = dateFormat.format(Date())
            }
            dao.saveAll(value)
        }
    }

    fun deleteById(code: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteById(code)
        }
    }

    fun deleteAll() = runBlocking {
        dao.deleteAll()
    }

    fun getById(code: Long): Appointment? {

        try {
            return object : AsyncTask<Any?, Any?, Any?>() {
                override fun doInBackground(objects: Array<Any?>): Any? {
                    return dao.getById(code)
                }
            }.execute().get() as Appointment?
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return null
    }

    fun getAll(): List<Appointment> {

        try {
            return object : AsyncTask<Any?, Any?, Any?>() {
                override fun doInBackground(objects: Array<Any?>): Any? {
                    return dao.getAll()
                }
            }.execute().get() as List<Appointment>
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return emptyList()
    }


    fun getAllFrom(maxDate : String ): List<Appointment> {

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
