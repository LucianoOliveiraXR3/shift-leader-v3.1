package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.*
import br.com.crearesistemas.shift_leader.db_service.model.AppointmentSaved

@Dao
interface AppointmentSavedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values: AppointmentSaved)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<AppointmentSaved>)

    @Query("DELETE FROM appointment_saved WHERE id = :id and origin like :origin")
    suspend fun deleteById(id: Long, origin: String)

    @Delete
    suspend fun deleteAll(value: List<AppointmentSaved>)

    @Query("SELECT * FROM appointment_saved tb WHERE tb.id = :id and tb.origin like :origin LIMIT 1")
    fun getById(id: Long, origin: String): AppointmentSaved?

    @Query("SELECT * FROM appointment_saved ")
    fun getAll(): List<AppointmentSaved>

    @Query("SELECT * FROM appointment_saved tb WHERE sent_to_cloud != 1 ORDER BY event_time ASC LIMIT 100")
    fun getAllNotSent(): List<AppointmentSaved>

    @Query("SELECT max(id) FROM appointment_saved WHERE origin like :origin")
    fun getMax(origin: String): Long

    @Query("SELECT max(event_time) FROM appointment_saved WHERE origin like :origin")
    fun getMaxDate(origin: String): String

}