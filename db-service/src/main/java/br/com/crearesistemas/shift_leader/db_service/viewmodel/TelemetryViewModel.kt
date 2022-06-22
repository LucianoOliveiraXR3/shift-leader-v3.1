package br.com.crearesistemas.shift_leader.db_service.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.crearesistemas.shift_leader.db_service.model.Telemetry
import br.com.crearesistemas.shift_leader.db_service.repository.TelemetryRepository

class TelemetryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TelemetryRepository(application)


    fun save(vararg values: Telemetry) : Int {
        repository.save(*values)
        return values.size
    }

    fun deleteById(id: Long, origin: String) = repository.deleteById(id, origin)

    fun deleteAll(value: List<Telemetry>) = repository.deleteAll(value)

    fun getById(id: Long, origin: String) = repository.getById(id, origin)

    fun getAll() = repository.getAll()

    fun getAllNotSent() = repository.getAllNotSent()

    fun getMaxDate( origin: String ) = repository.getMaxDate( origin )

}