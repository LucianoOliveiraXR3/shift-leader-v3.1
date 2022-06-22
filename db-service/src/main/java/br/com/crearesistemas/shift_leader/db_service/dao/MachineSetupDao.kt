package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.*
import br.com.crearesistemas.shift_leader.db_service.model.MachineSetup

@Dao
interface MachineSetupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values: MachineSetup)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<MachineSetup>)

    @Query("DELETE FROM machine_setup WHERE id = :id and origin like :origin")
    suspend fun deleteById(id: Long, origin: String)

    @Delete
    suspend fun deleteAll(value: List<MachineSetup>)

    @Query("SELECT * FROM machine_setup tb WHERE tb.id = :id and tb.origin like :origin LIMIT 1")
    fun getById(id: Long, origin: String): MachineSetup?

    @Query("SELECT * FROM machine_setup")
    fun getAll(): List<MachineSetup>

    @Query("SELECT * FROM machine_setup WHERE sent_to_cloud != 1 ORDER BY event_time ASC LIMIT 100")
    fun getAllNotSent(): List<MachineSetup>

    @Query("SELECT max(id) FROM machine_setup WHERE origin like :origin ")
    fun getMax(origin: String): Long

    @Query("SELECT max(event_time) FROM machine_setup WHERE origin = :origin ")
    fun getMaxDate(origin: String): String
}