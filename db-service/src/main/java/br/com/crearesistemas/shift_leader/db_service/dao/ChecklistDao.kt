package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.*
import br.com.crearesistemas.shift_leader.db_service.model.Checklist

@Dao
interface ChecklistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values: Checklist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<Checklist>)

    @Query("DELETE FROM checklist_items WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM checklist_items ")
    suspend fun deleteAll()

    @Query("SELECT * FROM checklist_items tb WHERE tb.id = :id LIMIT 1")
    fun getById(id: Long): Checklist?

    @Query("SELECT * FROM checklist_items LIMIT 100")
    fun getAll(): List<Checklist>

    @Query("SELECT * FROM checklist_items WHERE update_date > :maxDate ORDER BY update_date ASC")
    fun getAllFrom(maxDate : String): List<Checklist>

    @Query("SELECT max(update_date) FROM checklist_items ")
    fun getMaxDate(): String
}