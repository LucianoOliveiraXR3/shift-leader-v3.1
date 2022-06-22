package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.crearesistemas.shift_leader.db_service.model.MachineGroup

@Dao
interface MachineGroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values: MachineGroup)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<MachineGroup>)

    @Query("DELETE FROM machine_group WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM machine_group ")
    suspend fun deleteAll()

    @Query("SELECT * FROM machine_group tb WHERE tb.id = :id LIMIT 1")
    fun getById(id: Long): MachineGroup?

    @Query("SELECT * FROM machine_group")
    fun getAll(): List<MachineGroup>

    @Query("SELECT max(update_date) FROM machine_group ")
    fun getMaxDate(): String

    @Query("SELECT * FROM machine_group WHERE update_date > :maxDate ORDER BY update_date ASC")
    fun getAllFrom(maxDate : String): List<MachineGroup>
}
