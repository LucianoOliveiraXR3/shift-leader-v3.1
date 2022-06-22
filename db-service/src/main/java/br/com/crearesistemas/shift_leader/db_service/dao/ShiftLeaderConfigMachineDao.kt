package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.*
import br.com.crearesistemas.shift_leader.db_service.model.ShiftLeaderConfigMachine

@Dao
interface ShiftLeaderConfigMachineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values: ShiftLeaderConfigMachine)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<ShiftLeaderConfigMachine>)

    @Query("DELETE FROM shift_leader_config_machine WHERE id = :id ")
    suspend fun deleteById(id: Long)

    @Delete
    suspend fun deleteAll(value: List<ShiftLeaderConfigMachine>)

    @Query("SELECT * FROM shift_leader_config_machine tb WHERE tb.id = :id LIMIT 1")
    fun getById(id: Long): ShiftLeaderConfigMachine?

    @Query("SELECT * FROM shift_leader_config_machine tb WHERE tb.ssid = :ssid LIMIT 1")
    fun getBySsid(ssid: String): ShiftLeaderConfigMachine?


    @Query("SELECT * FROM shift_leader_config_machine")
    fun getAll(): List<ShiftLeaderConfigMachine>

}