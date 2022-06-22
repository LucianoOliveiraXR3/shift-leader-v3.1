package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.*
import br.com.crearesistemas.shift_leader.db_service.model.Appointment

@Dao
interface AppointmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values: Appointment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<Appointment>)

    @Query("DELETE FROM appointment_list WHERE code = :code")
    suspend fun deleteById(code: Long)

    @Query("DELETE FROM appointment_list ")
    suspend fun deleteAll()

    @Query("SELECT * FROM appointment_list tb WHERE tb.code = :code LIMIT 1")
    fun getById(code: Long): Appointment?

    @Query("SELECT * FROM appointment_list LIMIT 100")
    fun getAll(): List<Appointment>

    @Query("SELECT * FROM appointment_list WHERE update_date > :maxDate ORDER BY update_date ASC")
    fun getAllFrom(maxDate : String): List<Appointment>

    @Query("SELECT max(update_date) FROM appointment_list ")
    fun getMaxDate(): String
}
