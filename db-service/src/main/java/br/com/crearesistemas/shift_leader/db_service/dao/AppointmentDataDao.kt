package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.*
import br.com.crearesistemas.shift_leader.db_service.model.AppointmentData

@Dao
interface AppointmentDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values:  AppointmentData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<AppointmentData>)

    @Query("DELETE FROM appointment_data WHERE id = :id ")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM appointment_data  ")
    suspend fun deleteAll()

    @Query("SELECT * FROM appointment_data tb WHERE tb.id = :id LIMIT 1")
    fun getById(id: Long):AppointmentData?

    @Query("SELECT * FROM appointment_data LIMIT 100")
    fun getAll(): List<AppointmentData>

    @Query("SELECT * FROM appointment_data WHERE update_date > :maxDate ORDER BY update_date ASC")
    fun getAllFrom(maxDate : String): List<AppointmentData>

    @Query("SELECT max(update_date) FROM appointment_data ")
    fun getMaxDate(): String
}