package br.com.crearesistemas.shift_leader.db_service.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.Expose
import java.time.OffsetDateTime

/**
 * transmission direction:
 * copilot -> tablet_machine -> shift_leader -> api_shift_leader
 */
@Entity(tableName = "telemetry_list", primaryKeys = ["id", "origin"])
@JsonIgnoreProperties(ignoreUnknown = true)
class Telemetry {

    @NonNull
    @ColumnInfo(name = "id")
    var id: Long? = null

    @NonNull
    @ColumnInfo(name = "origin")
    var origin: String? = null

    @ColumnInfo(name = "insert_date")
    var insertDate: String? = null

    @ColumnInfo(name = "ignition")
    var ignition: Boolean? = null

    @ColumnInfo(name = "hourmeter")
    var hourmeter: Long? = null

    @ColumnInfo(name = "hodometer")
    var hodometer: Long? = null

    @ColumnInfo(name = "latitude")
    var latitude: Double? = null

    @ColumnInfo(name = "longitude")
    var longitude: Double? = null

    @ColumnInfo(name = "rpm")
    var rpm: Long? = null

    @ColumnInfo(name = "speed")
    var speed: Long? = null

    @ColumnInfo(name = "fuel_level")
    var fuelLevel: Long? = null

    @ColumnInfo(name = "fuel_total")
    var fuelTotal: Long? = null

    @ColumnInfo(name = "sent_to_cloud")
    var sentToCloud: Boolean? = false

    @ColumnInfo(name = "collected_at")
    var collectedAt: String? = null

}