package br.com.crearesistemas.shift_leader.db_service.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.crearesistemas.shift_leader.db_service.model.FarmSector
import br.com.crearesistemas.shift_leader.db_service.repository.FarmSectorRepository

class FarmSectorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FarmSectorRepository(application)

    fun save(vararg values: FarmSector) = repository.save(*values)

    fun deleteById(id: Long) = repository.deleteById(id)

    fun deleteAll() = repository.deleteAll()

    fun getById(id: Long) = repository.getById(id)

    fun getAll() = repository.getAll()

    fun getAllFrom( maxDate : String ) = repository.getAllFrom( maxDate )

    fun getMaxDate() = repository.getMaxDate()
}