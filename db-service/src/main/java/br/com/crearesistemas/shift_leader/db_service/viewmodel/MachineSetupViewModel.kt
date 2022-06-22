package br.com.crearesistemas.shift_leader.db_service.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.crearesistemas.shift_leader.db_service.model.MachineSetup
import br.com.crearesistemas.shift_leader.db_service.repository.MachineSetupRepository

class MachineSetupViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MachineSetupRepository(application)


    fun save(vararg values: MachineSetup): Int {
        repository.save(*values)
        return values.size
    }

    fun deleteById(id: Long, origin: String) = repository.deleteById(id, origin)

    fun deleteAll(value: List<MachineSetup>) = repository.deleteAll(value)

    fun getById(id: Long, origin: String) = repository.getById(id, origin)

    fun getAll() = repository.getAll()

    fun getAllNotSent() = repository.getAllNotSent()

    fun getMax( origin: String ) = repository.getMax( origin )

    fun getMaxDate( origin: String ) = repository.getMaxDate( origin )

}