package br.com.crearesistemas.shift_leader.db_service.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.Expose
import java.time.OffsetDateTime

/**
 * transmission direction:
 * copilot -> tablet_machine -> shift_leader -> api_shift_leader
 */
@Entity(tableName = "telemetry_alert", primaryKeys = ["pgn", "event_time", "origin"])
class TelemetryAlert {

    @NonNull
    @ColumnInfo(name = "pgn")
    var pgn: Long? = null

    @NonNull
    @ColumnInfo(name = "event_time")
    var eventTime: String? = null

    @NonNull
    @ColumnInfo(name = "origin")
    var origin: String? = null

    @ColumnInfo(name = "value")
    var value: Long? = null

    @ColumnInfo(name = "sent_to_cloud")
    var sentToCloud: Boolean? = false

    @ColumnInfo(name = "collected_at")
    var collectedAt: String? = null

}