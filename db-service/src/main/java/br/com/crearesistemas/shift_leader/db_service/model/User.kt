package br.com.crearesistemas.shift_leader.db_service.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * transmission direction
 * creare_cloud -> api_shift_leader -> shift_leader
 */
@Entity(tableName = "user_list", primaryKeys = ["id"])
class User {

    @NonNull
    @ColumnInfo(name = "id")
    var id: Long? = null

    @ColumnInfo(name = "username")
    var username: String? = null

    @ColumnInfo(name = "password")
    var password: String? = null

    /**
     *  values = ROLE_ADMIN || ROLE_USER
     *  format multi = ROLE_1,ROLE_2,ROLE_3
     */
    @ColumnInfo(name = "roles")
    var roles: String? = null

    @ColumnInfo(name = "collected_at")
    var collectedAt: String? = null

    @ColumnInfo(name = "update_date")
    var updateDate: String? = null

    @ColumnInfo(name = "enabled")
    var enabled: Boolean? = true

    override fun toString(): String {
        return "$id - $username ($updateDate) : $enabled"
    }

    fun getAllRoles(): List<String>? {
        return this.roles?.split(",")
    }

    fun isAdmin(): Boolean {
        return this.roles?.contains("ROLE_ADMIN") ?: false
    }

}
