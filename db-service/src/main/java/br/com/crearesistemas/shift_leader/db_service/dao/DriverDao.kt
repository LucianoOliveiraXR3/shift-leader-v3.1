package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.*
import br.com.crearesistemas.shift_leader.db_service.model.Driver

@Dao
interface DriverDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values: Driver)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<Driver>)

    @Query("DELETE FROM driver_list WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM driver_list ")
    suspend fun deleteAll()

    @Query("SELECT * FROM driver_list tb WHERE tb.id = :id LIMIT 1")
    fun getById(id: Long): Driver?

    @Query("SELECT * FROM driver_list")
    fun getAll(): List<Driver>

    @Query("SELECT * FROM driver_list WHERE update_date > :maxDate ORDER BY update_date ASC")
    fun getAllFrom(maxDate : String): List<Driver>

    @Query("SELECT max(update_date) FROM driver_list ")
    fun getMaxDate(): String

}