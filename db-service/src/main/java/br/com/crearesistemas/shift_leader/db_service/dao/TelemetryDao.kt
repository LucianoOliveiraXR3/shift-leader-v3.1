package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.*
import br.com.crearesistemas.shift_leader.db_service.model.Telemetry

@Dao
interface TelemetryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values: Telemetry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<Telemetry>)

    @Query("DELETE FROM telemetry_list WHERE id = :id and origin like :origin")
    suspend fun deleteById(id: Long, origin: String)

    @Delete
    suspend fun deleteAll(value: List<Telemetry>)

    @Query("SELECT * FROM telemetry_list tb WHERE tb.id = :id and tb.origin like :origin LIMIT 1")
    fun getById(id: Long, origin: String): Telemetry?

    @Query("SELECT * FROM telemetry_list")
    fun getAll(): List<Telemetry>

    @Query("SELECT * FROM telemetry_list WHERE sent_to_cloud != 1 ORDER BY insert_date ASC LIMIT 100")
    fun getAllNotSent(): List<Telemetry>

    @Query("SELECT max(insert_date) FROM telemetry_list WHERE origin = :origin ")
    fun getMaxDate(origin: String): String

}