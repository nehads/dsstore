package com.nehads.friends.di.respository_helper.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/* User that stores data of user name and email */
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var name: String,
    var email: String
)