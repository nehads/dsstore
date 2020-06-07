package com.nehads.friends.di.respository_helper.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nehads.friends.di.respository_helper.local.db.dao.UserDao
import com.nehads.friends.di.respository_helper.local.db.entity.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}