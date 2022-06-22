package br.com.crearesistemas.shift_leader.db_service.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.crearesistemas.shift_leader.db_service.model.ShiftLeaderConfig
import br.com.crearesistemas.shift_leader.db_service.repository.MachineRepository
import br.com.crearesistemas.shift_leader.db_service.repository.ShiftLeaderConfigRepository

class ShiftLeaderConfigViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShiftLeaderConfigRepository(application)

    fun save(vararg values: ShiftLeaderConfig) = repository.save(*values)

    fun deleteById(id: Long) = repository.deleteById(id)

    fun deleteAll(value: List<ShiftLeaderConfig>) = repository.deleteAll(value)

    fun getById(id: Long) = repository.getById(id)

    fun getAll() = repository.getAll()

}