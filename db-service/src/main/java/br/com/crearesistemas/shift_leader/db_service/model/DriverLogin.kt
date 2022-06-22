package br.com.crearesistemas.shift_leader.db_service.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.Expose
import java.time.OffsetDateTime

/**
 * transmission direction:
 * creare_cloud -> api_shift_leader -> shift_leader -> tablet_machine
 */
@Entity(tableName = "driver_login", primaryKeys = ["driver_id", "start_date", "origin"])
class DriverLogin {

    @NonNull
    @ColumnInfo(name = "driver_id")
    var driverId: Long? = null

    @NonNull
    @ColumnInfo(name = "start_date")
    var startDate: String? = null

    @NonNull
    @ColumnInfo(name = "origin")
    var origin: String? = null

    @ColumnInfo(name = "final_date")
    var finalDate: String? = null

    @ColumnInfo(name = "sent_to_cloud")
    var sentToCloud: Boolean? = false

    @ColumnInfo(name = "collected_at")
    var collectedAt: String? = null
}