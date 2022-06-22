package br.com.crearesistemas.shift_leader.db_service.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.crearesistemas.shift_leader.db_service.model.AppointmentGroup
import br.com.crearesistemas.shift_leader.db_service.repository.AppointmentGroupRepository

class AppointmentGroupViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppointmentGroupRepository(application)

    fun save(vararg values: AppointmentGroup) = repository.save(*values)

    fun deleteById(code: Long) = repository.deleteById(code)

    fun deleteAll() = repository.deleteAll()

    fun getById(code: Long) = repository.getById(code)

    fun getAll() = repository.getAll()

    fun getAllFrom( maxDate : String ) = repository.getAllFrom( maxDate )

    fun getMaxDate() = repository.getMaxDate()
}