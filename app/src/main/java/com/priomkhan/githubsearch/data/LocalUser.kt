package com.priomkhan.githubsearch.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class LocalUser(
                    @PrimaryKey(autoGenerate = true)
                    val userId: Int,
                    val userName: String,
                    val password: String) {
}