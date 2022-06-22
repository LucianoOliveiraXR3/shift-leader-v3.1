package br.com.crearesistemas.shift_leader.db_service.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import java.time.OffsetDateTime

/**
 * transmission direction:
 * creare_cloud -> api_shift_leader -> shift_leader -> tablet_machine
 */
@Entity(tableName = "machine_mechanization_type")
class MachineMechanizationType {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: Long? = null

    /**
     * origin = CLOUD
     */
    @ColumnInfo(name = "origin")
    var origin: String? = null

    @ColumnInfo(name = "description_primary")
    var descriptionPrimary: String? = null

    @ColumnInfo(name = "description_secondary")
    var descriptionSecondary: String? = null

    @ColumnInfo(name = "equipment_id")
    var equipmentId: Long? = null

    @ColumnInfo(name = "collected_at")
    var collectedAt: String? = null

    @ColumnInfo(name = "update_date")
    var updateDate: String? = null

    @ColumnInfo(name = "enabled")
    var enabled: Boolean? = true

}