package br.com.crearesistemas.shift_leader.db_service.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@Entity(tableName = "appointment_machine_type", primaryKeys = ["id"])
@JsonIgnoreProperties(ignoreUnknown = true)
class AppointmentMachineType {

    @ColumnInfo(name = "id")
    var id : Long? = null

    @ColumnInfo(name = "machine_type_id")
    var machineType : Long? = null

    @ColumnInfo(name = "appointment_code")
    var appointmentCode : Long? = null

    @ColumnInfo(name = "update_date")
    var updateDate: String? = null

    @ColumnInfo(name = "enabled")
    var enabled: Boolean? = true
}