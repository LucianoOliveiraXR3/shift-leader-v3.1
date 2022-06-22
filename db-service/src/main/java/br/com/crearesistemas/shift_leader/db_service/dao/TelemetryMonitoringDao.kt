package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.*
import br.com.crearesistemas.shift_leader.db_service.model.TelemetryMonitoring

@Dao
interface TelemetryMonitoringDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values: TelemetryMonitoring)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<TelemetryMonitoring>)

    @Query("DELETE FROM telemetry_monitoring WHERE pgn = :pgn")
    suspend fun deleteById(pgn: Long)

    @Delete
    suspend fun deleteAll(value: List<TelemetryMonitoring>)

    @Query("SELECT * FROM telemetry_monitoring tb WHERE tb.pgn = :pgn LIMIT 1")
    fun getById(pgn: Long): TelemetryMonitoring?

    @Query("SELECT * FROM telemetry_monitoring")
    fun getAll():  List<TelemetryMonitoring>

    @Query("SELECT * FROM telemetry_monitoring WHERE update_date > :maxDate ORDER BY update_date ASC")
    fun getAllFrom(maxDate : String): List<TelemetryMonitoring>

    @Query("SELECT max(update_date) FROM telemetry_monitoring ")
    fun getMaxDate(): String

}
