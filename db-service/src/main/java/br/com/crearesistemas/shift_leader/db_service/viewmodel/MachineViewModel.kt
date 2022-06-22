package br.com.crearesistemas.shift_leader.db_service.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.crearesistemas.shift_leader.db_service.model.Machine
import br.com.crearesistemas.shift_leader.db_service.repository.MachineRepository

class MachineViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MachineRepository(application)

    fun save(vararg values: Machine) = repository.save(*values)

    fun deleteById(id: Long) = repository.deleteById(id)

    fun updateSyncDate(description: String, syncDate: String) = repository.updateSyncDate(description, syncDate)

    fun deleteAll(value: List<Machine>) = repository.deleteAll(value)

    fun getById(id: Long) = repository.getById(id)

    fun getAll() = repository.getAll()

    fun getAllFromSignalLevel() = repository.getAllFromSignalLevel()

    fun getMaxDate() = repository.getMaxDate()

}