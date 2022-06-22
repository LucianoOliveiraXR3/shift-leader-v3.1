package br.com.crearesistemas.shift_leader.db_service.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.crearesistemas.shift_leader.db_service.model.DriverLogin
import br.com.crearesistemas.shift_leader.db_service.repository.DriverLoginRepository
import java.time.OffsetDateTime

class DriverLoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DriverLoginRepository(application)


    fun save(vararg values: DriverLogin) : Int {
        repository.save(*values)
        return values.size
    }

    fun deleteById(driverId: Long, startDate: OffsetDateTime, origin: String) = repository.deleteById(driverId, startDate, origin)

    fun deleteAll(value: List<DriverLogin>) = repository.deleteAll(value)

    fun getById(driverId: Long, startDate: OffsetDateTime, origin: String) = repository.getById(driverId, startDate, origin)

    fun getAll() = repository.getAll()

    fun getAllNotSent() = repository.getAllNotSent()

    fun getMaxDate( origin: String ) = repository.getMaxDate( origin )

}