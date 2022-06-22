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
@Entity(tableName = "production_harvester", primaryKeys = ["insert_date", "origin"])
class ProductionHarvester {

    @NonNull
    @ColumnInfo(name = "insert_date")
    var insertDate: String? = null

    @NonNull
    @ColumnInfo(name = "origin")
    var origin: String? = null

    @ColumnInfo(name = "n_stems")
    var stems: Int? = null

    @ColumnInfo(name = "volume")
    var volume: Float? = null

    @ColumnInfo(name = "sent_to_cloud")
    var sentToCloud: Boolean? = false

    @ColumnInfo(name = "collected_at")
    var collectedAt: String? = null

}