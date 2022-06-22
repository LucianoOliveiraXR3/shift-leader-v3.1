package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.*
import br.com.crearesistemas.shift_leader.db_service.model.TabletConfiguration

@Dao
interface TabletConfigurationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values: TabletConfiguration)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<TabletConfiguration>)

    @Query("DELETE FROM tablet_configuration WHERE `key` = :key")
    suspend fun deleteById(key: String)

    @Delete
    suspend fun deleteAll(value: List<TabletConfiguration>)

    @Query("SELECT * FROM tablet_configuration tb WHERE tb.`key` = :key LIMIT 1")
    fun getById(key: String): TabletConfiguration?

    @Query("SELECT * FROM tablet_configuration")
    fun getAll(): List<TabletConfiguration>

}