package br.com.crearesistemas.shift_leader.db_service.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.Expose
import java.time.OffsetDateTime

/**
 * transmission direction:
 * tablet_machine -> shift_leader -> api_shift_leader
 */
@Entity(tableName = "appointment_saved", primaryKeys = ["id", "origin"])
class AppointmentSaved {

    @NonNull
    @ColumnInfo(name = "id")
    var id: Long? = null

    @NonNull
    @ColumnInfo(name = "origin")
    var origin: String? = null

    @ColumnInfo(name = "appointment_code")
    var appointmentCode: Long? = null

    @ColumnInfo(name = "group_code")
    var groupCode: String? = null

    @ColumnInfo(name = "driver_id")
    var driverId: Long? = null

    @ColumnInfo(name = "event_time")
    var eventTime: String? = null

    @ColumnInfo(name = "telemetry_id")
    var telemetryId: Long? = null

    @ColumnInfo(name = "data")
    var data: String? = null

    @ColumnInfo(name = "sent_to_cloud")
    var sentToCloud: Boolean? = false

    @ColumnInfo(name = "collected_at")
    var collectedAt: String? = null

}