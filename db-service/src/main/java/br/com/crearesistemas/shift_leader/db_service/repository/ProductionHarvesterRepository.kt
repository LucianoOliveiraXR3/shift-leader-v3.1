package br.com.crearesistemas.shift_leader.db_service.repository
import android.app.Application
import android.os.AsyncTask
import br.com.crearesistemas.shift_leader.db_service.AppDatabase
import br.com.crearesistemas.shift_leader.db_service.model.ProductionHarvester
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.*
import java.util.concurrent.ExecutionException

class ProductionHarvesterRepository(application: Application) {

    private val db = AppDatabase.getInstance(application)
    private val dao = db!!.productionHarvesterDao()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())

    fun save(vararg values: ProductionHarvester) = runBlocking {
        values.forEach {
            it.collectedAt = dateFormat.format(Date())
        }
        dao.save(*values)
    }

    fun deleteById(insertDate: OffsetDateTime, origin: String) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteById(insertDate, origin)
        }
    }

    fun deleteAll(value: List<ProductionHarvester>) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAll(value)
        }
    }

    fun getById(insertDate: OffsetDateTime, origin: String): ProductionHarvester? {

        try {
            return object : AsyncTask<Any?, Any?, Any?>() {
                override fun doInBackground(objects: Array<Any?>): Any? {
                    return dao.getById(insertDate,origin)
                }
            }.execute().get() as ProductionHarvester?
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return null
    }

    fun getAll(): List<ProductionHarvester> {

        try {
            return object : AsyncTask<Any?, Any?, Any?>() {
                override fun doInBackground(objects: Array<Any?>): Any? {
                    return dao.getAll()
                }
            }.execute().get() as List<ProductionHarvester>
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
    fun getAllNotSent(): List<ProductionHarvester>
    {
        try {
            return dao.getAllNotSent()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }


    /**
     *
     */
    fun getMaxDate(origin: String): String
    {
        try {
            var maxDate = dao.getMaxDate(origin)
            if(!maxDate.isNullOrEmpty()) {
                return maxDate
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return "2020-01-01T00:00:00.000-0000"
    }

}