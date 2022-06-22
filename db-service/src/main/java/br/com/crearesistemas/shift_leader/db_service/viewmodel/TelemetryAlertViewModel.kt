package br.com.crearesistemas.shift_leader.db_service.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.crearesistemas.shift_leader.db_service.model.TelemetryAlert
import br.com.crearesistemas.shift_leader.db_service.repository.TelemetryAlertRepository
import java.time.OffsetDateTime

class TelemetryAlertViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TelemetryAlertRepository(application)

    fun save(vararg values: TelemetryAlert) = repository.save(*values)

    fun deleteById(pgn: Long, eventTime: OffsetDateTime, origin: String) = repository.deleteById(pgn, eventTime, origin)

    fun deleteAll(value: List<TelemetryAlert>) = repository.deleteAll(value)

    fun getById(pgn: Long, eventTime: OffsetDateTime, origin: String) = repository.getById(pgn, eventTime, origin)

    fun getAll() = repository.getAll()

    fun getMaxDate( origin: String ) = repository.getMaxDate( origin )

    fun getAllNotSent() = repository.getAllNotSent()

}