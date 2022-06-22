package br.com.crearesistemas.shift_leader.db_service.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity(tableName = "tablet_configuration")
class TabletConfiguration {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "key")
    var key: String? = null

    @NonNull
    @ColumnInfo(name = "value")
    var value: String? = null

    @NonNull
    @ColumnInfo(name = "description")
    var description: String? = null

    @ColumnInfo(name = "received_at")
    var receivedAt: String? = null

}