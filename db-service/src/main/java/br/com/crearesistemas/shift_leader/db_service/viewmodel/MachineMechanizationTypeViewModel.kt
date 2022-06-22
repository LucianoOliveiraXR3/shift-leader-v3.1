package br.com.crearesistemas.shift_leader.db_service.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.crearesistemas.shift_leader.db_service.model.MachineMechanizationType
import br.com.crearesistemas.shift_leader.db_service.repository.MachineMechanizationTypeRepository

class MachineMechanizationTypeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MachineMechanizationTypeRepository(application)

    fun save(vararg values: MachineMechanizationType) = repository.save(*values)

    fun deleteById(id: Long) = repository.deleteById(id)

    fun deleteAll() = repository.deleteAll()

    fun getById(id: Long) = repository.getById(id)

    fun getAll() = repository.getAll()

    fun getAllFrom( maxDate : String ) = repository.getAllFrom( maxDate )

    fun getMaxDate() = repository.getMaxDate()
}