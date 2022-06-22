package br.com.crearesistemas.shift_leader.db_service.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.crearesistemas.shift_leader.db_service.model.ProductionHarvester
import br.com.crearesistemas.shift_leader.db_service.repository.ProductionHarvesterRepository
import java.time.OffsetDateTime

class ProductionHarvesterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProductionHarvesterRepository(application)

    fun save(vararg values: ProductionHarvester) = repository.save(*values)

    fun deleteById(insertDate: OffsetDateTime, origin: String) = repository.deleteById(insertDate, origin)

    fun deleteAll(value: List<ProductionHarvester>) = repository.deleteAll(value)

    fun getById(insertDate: OffsetDateTime, origin: String) = repository.getById(insertDate, origin)

    fun getAll() = repository.getAll()

    fun getAllNotSent() = repository.getAllNotSent()

    fun getMaxDate( origin: String ) = repository.getMaxDate( origin )

}