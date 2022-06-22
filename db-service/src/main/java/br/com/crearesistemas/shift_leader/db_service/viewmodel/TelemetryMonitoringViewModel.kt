package br.com.crearesistemas.shift_leader.db_service.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.crearesistemas.shift_leader.db_service.model.TelemetryMonitoring
import br.com.crearesistemas.shift_leader.db_service.repository.TelemetryMonitoringRepository

class TelemetryMonitoringViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TelemetryMonitoringRepository(application)

    fun save(vararg values: TelemetryMonitoring) = repository.save(*values)

    fun deleteById(pgn: Long) = repository.deleteById(pgn)

    fun deleteAll(value: List<TelemetryMonitoring>) = repository.deleteAll(value)

    fun getById(pgn: Long) = repository.getById(pgn)

    fun getAll() = repository.getAll()

    fun getAllFrom( maxDate : String ) = repository.getAllFrom( maxDate )

    fun getMaxDate() = repository.getMaxDate()
}