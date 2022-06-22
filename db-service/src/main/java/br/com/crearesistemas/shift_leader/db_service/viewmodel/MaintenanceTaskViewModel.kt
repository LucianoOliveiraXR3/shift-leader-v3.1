package br.com.crearesistemas.shift_leader.db_service.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.crearesistemas.shift_leader.db_service.model.MaintenanceTask
import br.com.crearesistemas.shift_leader.db_service.repository.MaintenanceTaskRepository

class MaintenanceTaskViewModel(application: Application) : AndroidViewModel(application){


    private val repository = MaintenanceTaskRepository(application)

    fun save(vararg values: MaintenanceTask) : Int{
        repository.save(*values)
        return values.size
    }

    fun deleteById(id : Long) = repository.deleteById(id)

    fun deleteAll(value: List<MaintenanceTask>) = repository.deleteAll(value)

    fun getByid(id: Long) = repository.getById(id)

    fun getLastVersion() = repository.getLastVersion()

    fun getAll() = repository.getAll()
}