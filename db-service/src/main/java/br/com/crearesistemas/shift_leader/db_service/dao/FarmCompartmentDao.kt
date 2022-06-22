package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.*
import br.com.crearesistemas.shift_leader.db_service.model.FarmCompartment

@Dao
interface FarmCompartmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values: FarmCompartment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<FarmCompartment>)

    @Query("DELETE FROM farm_compartment WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM farm_compartment ")
    suspend fun deleteAll()

    @Query("SELECT * FROM farm_compartment tb WHERE tb.id = :id LIMIT 1")
    fun getById(id: Long): FarmCompartment?

    @Query("SELECT * FROM farm_compartment")
    fun getAll(): List<FarmCompartment>

    @Query("SELECT * FROM farm_compartment WHERE update_date > :maxDate ORDER BY update_date ASC")
    fun getAllFrom(maxDate : String): List<FarmCompartment>

    @Query("SELECT max(update_date) FROM farm_compartment ")
    fun getMaxDate(): String
}
