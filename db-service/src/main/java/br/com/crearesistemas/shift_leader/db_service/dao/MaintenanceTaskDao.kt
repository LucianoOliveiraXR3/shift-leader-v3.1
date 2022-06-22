package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.*
import br.com.crearesistemas.shift_leader.db_service.model.MaintenanceTask

@Dao
interface MaintenanceTaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values: MaintenanceTask)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<MaintenanceTask>)

    @Query("DELETE FROM maintenance_tasks WHERE id = :id ")
    suspend fun deleteById(id: Long)

    @Delete
    suspend fun deleteAll(value: List<MaintenanceTask>)

    @Query("SELECT * FROM maintenance_tasks tb WHERE tb.id = :id LIMIT 1")
    fun getById(id: Long): MaintenanceTask?

    @Query("SELECT max(version) FROM maintenance_tasks ")
    fun getLatestVersion(): Long?

    @Query("SELECT * FROM maintenance_tasks LIMIT 100")
    fun getAll(): List<MaintenanceTask>

}