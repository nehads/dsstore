package com.nehads.friends.di.repository

import com.nehads.friends.di.respository_helper.local.db.DatabaseHelper
import com.nehads.friends.di.respository_helper.remote.RemoteHelper
import com.nehads.friends.di.respository_helper.shared_preference.PreferenceHelper
import io.reactivex.Single

interface DataRepository : RemoteHelper, PreferenceHelper, DatabaseHelper {
    fun nukeTheUser(): Single<Boolean>
}