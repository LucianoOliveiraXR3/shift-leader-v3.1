package br.com.crearesistemas.shift_leader.db_service.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "machine_list", primaryKeys = ["id"])
class Machine {

    @ColumnInfo(name = "id")
    var id: Long? = null

    @ColumnInfo(name = "description")
    var description: String? = null

    @ColumnInfo(name = "hotspot_ssid")
    var hotspotSsid: String? = null

    @ColumnInfo(name = "hotspot_password")
    var hotspotPassword: String? = null

    @ColumnInfo(name = "collected_at")
    var collectedAt: String? = null

    @ColumnInfo(name = "update_date")
    var updateDate: String? = null

    @ColumnInfo(name = "enabled")
    var enabled: Boolean? = true

    @ColumnInfo(name = "signal")
    var signal : Int = -999;

    @ColumnInfo(name = "bssid")
    var bssid : String? = null;

    @ColumnInfo(name = "last_sync_date")
    var lastSyncDate: String? = null


    override fun toString(): String {
        return "$id - $description - $hotspotSsid ($updateDate) : $enabled ($signal)"
    }
}