package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.*
import br.com.crearesistemas.shift_leader.db_service.model.AppointmentSemiMechanized

@Dao
interface AppointmentSemiMechanizedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values: AppointmentSemiMechanized)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<AppointmentSemiMechanized>)

    @Query("DELETE FROM appointment_semi_mechanized WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Delete
    suspend fun deleteAll(value: List<AppointmentSemiMechanized>)

    @Query("SELECT * FROM appointment_semi_mechanized tb WHERE tb.id = :id LIMIT 1")
    fun getById(id: Long): AppointmentSemiMechanized?

    @Query("SELECT * FROM appointment_semi_mechanized")
    fun getAll(): List<AppointmentSemiMechanized>

    @Query("SELECT * FROM appointment_semi_mechanized WHERE sent_to_cloud != 1 ORDER BY collected_at ASC LIMIT 100")
    fun getAllNotSent(): List<AppointmentSemiMechanized>

}