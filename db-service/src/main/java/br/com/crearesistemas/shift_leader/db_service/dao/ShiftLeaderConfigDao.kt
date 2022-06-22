package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.*
import br.com.crearesistemas.shift_leader.db_service.model.ShiftLeaderConfig

@Dao
interface ShiftLeaderConfigDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values: ShiftLeaderConfig)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<ShiftLeaderConfig>)

    @Query("DELETE FROM shift_leader_config WHERE id = :id ")
    suspend fun deleteById(id: Long)

    @Delete
    suspend fun deleteAll(value: List<ShiftLeaderConfig>)

    @Query("SELECT * FROM shift_leader_config tb WHERE tb.id = :id LIMIT 1")
    fun getById(id: Long): ShiftLeaderConfig?

    @Query("SELECT * FROM shift_leader_config")
    fun getAll(): List<ShiftLeaderConfig>

}