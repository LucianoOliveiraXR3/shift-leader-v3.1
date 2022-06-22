package br.com.crearesistemas.shift_leader.db_service.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.crearesistemas.shift_leader.db_service.model.MachineGroup
import br.com.crearesistemas.shift_leader.db_service.repository.MachineGroupRepository

class MachineGroupViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MachineGroupRepository(application)

    fun save(vararg values: MachineGroup) = repository.save(*values)

    fun deleteById(id: Long) = repository.deleteById(id)

    fun deleteAll() = repository.deleteAll()

    fun getById(id: Long) = repository.getById(id)

    fun getAll() = repository.getAll()

    fun getMaxDate() = repository.getMaxDate()

    fun getAllFrom( maxDate : String ) = repository.getAllFrom( maxDate )
}