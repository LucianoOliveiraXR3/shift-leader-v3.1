package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.*
import br.com.crearesistemas.shift_leader.db_service.model.MachineMechanizationType

@Dao
interface MachineMechanizationTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values: MachineMechanizationType)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<MachineMechanizationType>)

    @Query("DELETE FROM machine_mechanization_type WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM machine_mechanization_type ")
    suspend fun deleteAll()

    @Query("SELECT * FROM machine_mechanization_type tb WHERE tb.id = :id LIMIT 1")
    fun getById(id: Long): MachineMechanizationType?

    @Query("SELECT * FROM machine_mechanization_type")
    fun getAll(): List<MachineMechanizationType>

    @Query("SELECT * FROM machine_mechanization_type WHERE update_date > :maxDate ORDER BY update_date ASC")
    fun getAllFrom(maxDate : String): List<MachineMechanizationType>

    @Query("SELECT max(update_date) FROM machine_mechanization_type ")
    fun getMaxDate(): String
}
