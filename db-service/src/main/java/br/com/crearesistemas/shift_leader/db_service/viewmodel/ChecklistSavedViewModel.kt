package br.com.crearesistemas.shift_leader.db_service.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.crearesistemas.shift_leader.db_service.model.ChecklistSaved
import br.com.crearesistemas.shift_leader.db_service.repository.ChecklistSavedRepository

class ChecklistSavedViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ChecklistSavedRepository(application)


    fun save(vararg values: ChecklistSaved) : Int {
        repository.save(*values)
        return values.size
    }

    fun deleteById(id: Long, origin: String) = repository.deleteById(id, origin)

    fun deleteAll(value: List<ChecklistSaved>) = repository.deleteAll(value)

    fun getById(id: Long, origin: String) = repository.getById(id, origin)

    fun getAll() = repository.getAll()

    fun getAllNotSent() = repository.getAllNotSent()

    fun getMax(origin: String) = repository.getMax(origin)

    fun getMaxDate(origin: String) = repository.getMaxDate(origin)

}