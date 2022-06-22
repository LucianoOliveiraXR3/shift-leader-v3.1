package br.com.crearesistemas.shift_leader.db_service.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.crearesistemas.shift_leader.db_service.model.AppointmentMachineType
import br.com.crearesistemas.shift_leader.db_service.repository.AppointmentMachineTypeRepository

class AppointmentMachineTypeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppointmentMachineTypeRepository(application)

    fun save(vararg values: AppointmentMachineType) : Int {
        repository.save(*values)
        return  values.size
    }

    fun deleteById(id : Long) = repository.deleteById(id)

    fun deleteAll() = repository.deleteAll()

    fun getByid(id: Long) = repository.getById(id)

    fun getAll() = repository.getAll()

    fun getAllFrom( maxDate : String ) = repository.getAllFrom( maxDate )

    fun getMaxDate() = repository.getMaxDate()
}