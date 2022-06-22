package br.com.crearesistemas.shift_leader.db_service.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "shift_leader_config_machine")
class ShiftLeaderConfigMachine {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: Long? = null

    @ColumnInfo(name = "ssid")
    var ssid: String? = null

    @ColumnInfo(name = "password")
    var password: String? = null

    @ColumnInfo(name = "is_connected")
    var isConnected: Boolean? = false

    @ColumnInfo(name = "last_sync")
    var lastSyncTimestamp: String? = null

}