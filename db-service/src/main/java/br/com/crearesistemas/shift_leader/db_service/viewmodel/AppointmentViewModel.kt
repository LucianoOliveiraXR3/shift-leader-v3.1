package br.com.crearesistemas.shift_leader.db_service.viewmodel

import androidx.lifecycle.AndroidViewModel
import android.app.Application
import br.com.crearesistemas.shift_leader.db_service.model.Appointment
import br.com.crearesistemas.shift_leader.db_service.repository.AppointmentRepository

class AppointmentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppointmentRepository(application)

    fun save(vararg values: Appointment) = repository.save(*values)

    fun saveAll(value: List<Appointment>) = repository.saveAll(value)

    fun deleteById(code: Long) = repository.deleteById(code)

    fun deleteAll() = repository.deleteAll()

    fun getById(code: Long) = repository.getById(code)

    fun getAll() = repository.getAll()

    fun getAllFrom( maxDate : String ) = repository.getAllFrom( maxDate )

    fun getMaxDate() = repository.getMaxDate()
}