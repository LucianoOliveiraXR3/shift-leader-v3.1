package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.*
import br.com.crearesistemas.shift_leader.db_service.model.AppointmentMachineType

@Dao
interface AppointmentMachineTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values: AppointmentMachineType)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<AppointmentMachineType>)

    @Query("DELETE FROM appointment_machine_type WHERE id = :id ")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM appointment_machine_type  ")
    suspend fun deleteAll()

    @Query("SELECT * FROM appointment_machine_type tb WHERE tb.id = :id LIMIT 1")
    fun getById(id: Long): AppointmentMachineType?

    @Query("SELECT * FROM appointment_machine_type")
    fun getAll(): List<AppointmentMachineType>

    @Query("SELECT * FROM appointment_machine_type WHERE update_date > :maxDate ORDER BY update_date ASC")
    fun getAllFrom(maxDate : String): List<AppointmentMachineType>

    @Query("SELECT max(update_date) FROM appointment_machine_type ")
    fun getMaxDate(): String
}