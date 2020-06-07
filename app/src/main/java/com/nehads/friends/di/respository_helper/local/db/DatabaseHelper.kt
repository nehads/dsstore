package com.nehads.friends.di.respository_helper.local.db

import com.nehads.friends.di.respository_helper.local.db.entity.User
import io.reactivex.Single

interface DatabaseHelper {
    fun saveUserData(userDetail: User)

    fun deleteUser()

    fun getUserData(): Single<User>
}