package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.*
import br.com.crearesistemas.shift_leader.db_service.model.ChecklistSaved

@Dao
interface ChecklistSavedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values: ChecklistSaved)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<ChecklistSaved>)

    @Query("DELETE FROM checklist_saved WHERE id = :id and origin like :origin")
    suspend fun deleteById(id: Long, origin: String)

    @Delete
    suspend fun deleteAll(value: List<ChecklistSaved>)

    @Query("SELECT * FROM checklist_saved tb WHERE tb.id = :id and tb.origin like :origin LIMIT 1")
    fun getById(id: Long, origin: String): ChecklistSaved?

    @Query("SELECT * FROM checklist_saved")
    fun getAll(): List<ChecklistSaved>

    @Query("SELECT * FROM checklist_saved WHERE sent_to_cloud != 1 ORDER BY event_time ASC LIMIT 100")
    fun getAllNotSent(): List<ChecklistSaved>

    @Query("SELECT max(id) FROM checklist_saved WHERE origin like :origin")
    fun getMax(origin: String): Long

    @Query("SELECT max(event_time) FROM checklist_saved WHERE origin like :origin")
    fun getMaxDate(origin: String): String

}