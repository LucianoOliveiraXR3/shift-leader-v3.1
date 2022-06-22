package br.com.crearesistemas.shift_leader.db_service.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import java.time.OffsetDateTime
import java.util.*

/**
 * transmission direction:
 * shift_leader -> api_shift_leader
 */
@Entity(tableName = "appointment_semi_mechanized")
class AppointmentSemiMechanized {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    var id: Long? = null

    @NonNull
    @ColumnInfo(name = "origin")
    var origin: String? = null

    @ColumnInfo(name = "user_id")
    var userId: Long? = null

    @ColumnInfo(name = "type")
    var type: String? = null

    @ColumnInfo(name = "data")
    var data: String? = null

    @ColumnInfo(name = "sent_to_cloud")
    var sentToCloud: Boolean? = false

    @ColumnInfo(name = "collected_at")
    var collectedAt: String? = null

}