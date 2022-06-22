package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.*
import br.com.crearesistemas.shift_leader.db_service.model.MachineActivityType

@Dao
interface MachineActivityTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values: MachineActivityType)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<MachineActivityType>)

    @Query("DELETE FROM machine_activity_type WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM machine_activity_type ")
    suspend fun deleteAll()

    @Query("SELECT * FROM machine_activity_type tb WHERE tb.id = :id LIMIT 1")
    fun getById(id: Long): MachineActivityType?

    @Query("SELECT * FROM machine_activity_type")
    fun getAll(): List<MachineActivityType>

    @Query("SELECT * FROM machine_activity_type WHERE update_date > :maxDate ORDER BY update_date ASC")
    fun getAllFrom(maxDate : String): List<MachineActivityType>

    @Query("SELECT max(update_date) FROM machine_activity_type ")
    fun getMaxDate(): String
}
