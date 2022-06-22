package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.*
import br.com.crearesistemas.shift_leader.db_service.model.ProductionHarvester
import java.time.OffsetDateTime

@Dao
interface ProductionHarvesterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values: ProductionHarvester)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<ProductionHarvester>)

    @Query("DELETE FROM production_harvester WHERE insert_date = :insertDate and origin like :origin")
    suspend fun deleteById(insertDate: OffsetDateTime, origin: String)

    @Delete
    suspend fun deleteAll(value: List<ProductionHarvester>)

    @Query("SELECT * FROM production_harvester tb WHERE insert_date = :insertDate and origin like :origin LIMIT 1")
    fun getById(insertDate: OffsetDateTime, origin: String): ProductionHarvester?

    @Query("SELECT * FROM production_harvester")
    fun getAll(): List<ProductionHarvester>

    @Query("SELECT * FROM production_harvester WHERE sent_to_cloud != 1 ORDER BY insert_date ASC LIMIT 100")
    fun getAllNotSent(): List<ProductionHarvester>

    @Query("SELECT max(insert_date) FROM production_harvester WHERE origin = :origin ")
    fun getMaxDate(origin: String): String

}