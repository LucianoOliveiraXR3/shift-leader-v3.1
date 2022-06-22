package br.com.crearesistemas.shift_leader.db_service.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.crearesistemas.shift_leader.db_service.model.AppointmentSemiMechanized
import br.com.crearesistemas.shift_leader.db_service.repository.AppointmentSemiMechanizedRepository
import br.com.crearesistemas.shift_leader.db_service.util.NetworkUtil
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

class AppointmentSemiMechanizedViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppointmentSemiMechanizedRepository(application)


    fun save(vararg values: AppointmentSemiMechanized) = repository.save(*values)

    fun deleteById(id: Long) = repository.deleteById(id)

    fun deleteAll(value: List<AppointmentSemiMechanized>) = repository.deleteAll(value)

    fun getById(id: Long) = repository.getById(id)

    fun getAll() = repository.getAll()

    fun getAllNotSent() = repository.getAllNotSent()

}