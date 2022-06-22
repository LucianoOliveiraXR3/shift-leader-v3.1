package br.com.crearesistemas.shift_leader.db_service.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.crearesistemas.shift_leader.db_service.model.TabletConfiguration
import br.com.crearesistemas.shift_leader.db_service.repository.TabletConfigurationRepository

class TabletConfigurationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TabletConfigurationRepository(application)

    fun save(vararg values: TabletConfiguration) = repository.save(*values)

    fun deleteById(key: String) = repository.deleteById(key)

    fun deleteAll(value: List<TabletConfiguration>) = repository.deleteAll(value)

    fun getById(key: String) = repository.getById(key)

    fun getAll() = repository.getAll()

}