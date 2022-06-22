package br.com.crearesistemas.shift_leader.db_service.dao

import androidx.room.*
import br.com.crearesistemas.shift_leader.db_service.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg values: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(value: List<User>)

    @Query("DELETE FROM user_list WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Delete
    suspend fun deleteAll(value: List<User>)

    @Query("SELECT * FROM user_list tb WHERE tb.id = :id LIMIT 1")
    fun getById(id: Long): User?

    @Query("SELECT * FROM user_list")
    fun getAll(): List<User>

    @Query("SELECT * FROM user_list tb WHERE tb.username like :username and tb.password like :password LIMIT 1")
    fun login(username: String, password: String): User?

    @Query("SELECT max(update_date) FROM user_list ")
    fun getMaxDate(): String
}