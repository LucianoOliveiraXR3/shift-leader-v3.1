package br.com.crearesistemas.shift_leader.db_service.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.crearesistemas.shift_leader.db_service.model.User
import br.com.crearesistemas.shift_leader.db_service.repository.UserRepository

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)
    
    fun save(vararg values: User) = repository.save(*values)

    fun deleteById(id: Long) = repository.deleteById(id)

    fun deleteAll(value: List<User>) = repository.deleteAll(value)

    fun getById(id: Long) = repository.getById(id)

    fun getAll() = repository.getAll()

    fun login(username: String, password: String) = repository.login(username, password)

    fun getMaxDate() = repository.getMaxDate()
}