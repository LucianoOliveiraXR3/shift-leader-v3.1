package br.com.crearesistemas.shift_leader.db_service.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@Entity(tableName = "maintenance_tasks", primaryKeys = ["id"])
@JsonIgnoreProperties(ignoreUnknown = true)
class MaintenanceTask {

    @ColumnInfo(name = "id")
    var id : Long? = null

    @ColumnInfo(name = "version")
    var version : Long? = null

    @ColumnInfo(name = "type")
    var type : String? = null

    @ColumnInfo(name = "task_date")
    var taskDate : String? = null

}