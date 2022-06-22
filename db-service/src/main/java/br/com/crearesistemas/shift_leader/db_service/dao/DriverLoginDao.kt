package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.*
import br.com.crearesistemas.shift_leader.db_service.model.DriverLogin
import java.time.OffsetDateTime

@Dao
interface DriverLoginDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values: DriverLogin)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<DriverLogin>)

    @Query("DELETE FROM driver_login WHERE driver_id = :driverId and start_date = :startDate and origin like :origin")
    suspend fun deleteById(driverId: Long, startDate: OffsetDateTime, origin: String)

    @Delete
    suspend fun deleteAll(value: List<DriverLogin>)

    @Query("SELECT * FROM driver_login tb WHERE driver_id = :driverId and start_date = :startDate and origin like :origin LIMIT 1")
    fun getById(driverId: Long, startDate: OffsetDateTime, origin: String): DriverLogin?

    @Query("SELECT * FROM driver_login")
    fun getAll(): List<DriverLogin>

    @Query("SELECT * FROM driver_login WHERE sent_to_cloud != 1 ORDER BY start_date ASC LIMIT 100")
    fun getAllNotSent(): List<DriverLogin>

    @Query("SELECT max(start_date) FROM driver_login WHERE origin = :origin ")
    fun getMaxDate(origin: String): String

}