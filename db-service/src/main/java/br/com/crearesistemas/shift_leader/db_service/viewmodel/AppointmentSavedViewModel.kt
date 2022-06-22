package br.com.crearesistemas.shift_leader.db_service.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.crearesistemas.shift_leader.db_service.model.AppointmentSaved
import br.com.crearesistemas.shift_leader.db_service.repository.AppointmentSavedRepository


class AppointmentSavedViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppointmentSavedRepository(application)

    fun save(vararg values: AppointmentSaved) : Int {
        repository.save(*values)
        return values.size
    }

    fun deleteById(id: Long, origin: String) = repository.deleteById(id, origin)

    fun deleteAll(value: List<AppointmentSaved>) = repository.deleteAll(value)

    fun getById(id: Long, origin: String) = repository.getById(id, origin)

    fun getAll() = repository.getAll()

    fun getAllNotSent() = repository.getAllNotSent()

    fun getMax(origin: String) = repository.getMax(origin)

    fun getMaxDate(origin: String) = repository.getMaxDate(origin)

}