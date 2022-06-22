package br.com.crearesistemas.shift_leader.db_service.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@Entity(tableName = "appointment_data", primaryKeys = ["id"])
@JsonIgnoreProperties(ignoreUnknown = true)
class AppointmentData {

    @ColumnInfo(name = "id")
    var id : Long? = null

    @ColumnInfo(name = "name")
    var name : String? = null

    @ColumnInfo(name = "type")
    var type : Int? = null

    @ColumnInfo(name = "appointment_id")
    var appointmentId : Long? = null

    @ColumnInfo(name = "optional")
    var optional : String? = null

    @ColumnInfo(name = "update_date")
    var updateDate: String? = null

    @ColumnInfo(name = "enabled")
    var enabled: Boolean? = true
}