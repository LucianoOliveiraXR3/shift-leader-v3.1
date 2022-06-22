package br.com.crearesistemas.shift_leader.db_service.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.crearesistemas.shift_leader.db_service.model.ShiftLeaderConfigMachine
import br.com.crearesistemas.shift_leader.db_service.repository.MachineRepository
import br.com.crearesistemas.shift_leader.db_service.repository.ShiftLeaderConfigMachineRepository

class ShiftLeaderConfigMachineViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShiftLeaderConfigMachineRepository(application)

    fun save(vararg values: ShiftLeaderConfigMachine) = repository.save(*values)

    fun deleteById(id: Long) = repository.deleteById(id)

    fun deleteAll(value: List<ShiftLeaderConfigMachine>) = repository.deleteAll(value)

    fun getById(id: Long) = repository.getById(id)

    fun getAll() = repository.getAll()

    fun getBySsid( ssid: String ) = repository.getBySsid( ssid )

}