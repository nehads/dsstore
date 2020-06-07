package com.nehads.friends.di.respository_helper.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nehads.friends.di.respository_helper.local.db.entity.User
import io.reactivex.Single

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserData(userDetail: User): Long?

    @Query("DELETE FROM user_table")
    fun nukeUserData()

    @Query("SELECT * from user_table limit 1")
    fun getUserData(): Single<User>

}