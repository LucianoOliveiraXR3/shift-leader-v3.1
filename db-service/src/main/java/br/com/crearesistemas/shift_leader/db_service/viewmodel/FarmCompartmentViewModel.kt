package br.com.crearesistemas.shift_leader.db_service.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.crearesistemas.shift_leader.db_service.model.FarmCompartment
import br.com.crearesistemas.shift_leader.db_service.repository.FarmCompartmentRepository

class FarmCompartmentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FarmCompartmentRepository(application)

    fun save(vararg values: FarmCompartment) = repository.save(*values)

    fun deleteById(id: Long) = repository.deleteById(id)

    fun deleteAll() = repository.deleteAll()

    fun getById(id: Long) = repository.getById(id)

    fun getAll() = repository.getAll()

    fun getAllFrom( maxDate : String ) = repository.getAllFrom( maxDate )

    fun getMaxDate() = repository.getMaxDate()
}