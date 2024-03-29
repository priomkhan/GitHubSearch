package com.priomkhan.githubsearch.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * from  users")
    fun getAll(): List<LocalUser>

    @Insert
    suspend fun insertUser(user: LocalUser)

    @Query("DELETE from users")
    suspend fun deleteAll()
}