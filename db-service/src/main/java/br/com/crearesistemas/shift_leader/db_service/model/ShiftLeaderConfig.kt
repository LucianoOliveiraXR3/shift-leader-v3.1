package br.com.crearesistemas.shift_leader.db_service.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "shift_leader_config")
class ShiftLeaderConfig {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: Long? = null

    @ColumnInfo(name = "last_sync")
    var lastSyncTime: String? = null

    @ColumnInfo(name = "limit_yellow")
    var limitYellow: Int? = null

    @ColumnInfo(name = "limit_red")
    var limitRed: Int? = null

    @ColumnInfo(name = "machine_tablet_address")
    var machineTabletAddress: String? = null

    @ColumnInfo(name = "wifi_default_password")
    var wifiDefaultPassword: String? = null

    @ColumnInfo(name = "cloud_address")
    var cloudAddress: String? = null

}