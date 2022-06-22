package br.com.crearesistemas.shift_leader.db_service.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.Expose
import java.time.OffsetDateTime
import java.util.*

/**
 * transmission direction:
 * tablet_machine -> shift_leader -> api_shift_leader
 */
@Entity(tableName = "machine_setup", primaryKeys = ["id", "origin"])
class MachineSetup {

    @NonNull
    @ColumnInfo(name = "id")
    var id: Long? = null

    @NonNull
    @ColumnInfo(name = "origin")
    var origin: String? = null

    @ColumnInfo(name = "driver_id")
    var driverId: Long? = null

    @ColumnInfo(name = "mechanization_id")
    var mechanizationTypeId: Long? = null

    @ColumnInfo(name = "activity_id")
    var activityId: Long? = null

    @ColumnInfo(name = "compartment_id")
    var compartmentId: Long? = null

    @ColumnInfo(name = "event_time")
    var eventTime: String? = null

    @ColumnInfo(name = "telemetry_id")
    var telemetryId: Long? = null

    @ColumnInfo(name = "equipment_id")
    var equipmentId: Long? = null

    @ColumnInfo(name = "sent_to_cloud")
    var sentToCloud: Boolean? = false

    @ColumnInfo(name = "collected_at")
    var collectedAt: String? = null

    @ColumnInfo(name = "machine_group_id")
    var machineGroupId: Long? = null

}
