package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.*
import br.com.crearesistemas.shift_leader.db_service.model.TelemetryAlert
import java.time.OffsetDateTime

@Dao
interface TelemetryAlertDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values: TelemetryAlert)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<TelemetryAlert>)

    @Query("DELETE FROM telemetry_alert WHERE pgn = :pgn and event_time = :eventTime and origin like :origin")
    suspend fun deleteById(pgn: Long, eventTime: OffsetDateTime, origin: String)

    @Delete
    suspend fun deleteAll(value: List<TelemetryAlert>)

    @Query("SELECT * FROM telemetry_alert tb WHERE pgn = :pgn and event_time = :eventTime and origin like :origin LIMIT 1")
    fun getById(pgn: Long, eventTime: OffsetDateTime, origin: String): TelemetryAlert?

    @Query("SELECT * FROM telemetry_alert")
    fun getAll(): List<TelemetryAlert>

    @Query("SELECT * FROM telemetry_alert WHERE sent_to_cloud != 1 ORDER BY event_time ASC LIMIT 100")
    fun getAllNotSent(): List<TelemetryAlert>

    @Query("SELECT max(event_time) FROM telemetry_alert WHERE origin = :origin ")
    fun getMaxDate(origin: String): String


}