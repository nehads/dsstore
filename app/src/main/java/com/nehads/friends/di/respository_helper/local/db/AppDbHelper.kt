package com.nehads.friends.di.respository_helper.local.db

import android.util.Log
import com.nehads.friends.di.respository_helper.local.db.entity.User
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/* DB helper class that connects the request(add/delete) to actual query */
@Singleton
class AppDbHelper @Inject
constructor(
    private val mAppDatabase: AppDatabase,
    private val compositeDisposable: CompositeDisposable
) : DatabaseHelper {

    private val TAG = AppDbHelper::class.java.canonicalName

    override fun saveUserData(userDetail: User) {
        compositeDisposable.add(
            Completable.fromAction { mAppDatabase.userDao().insertUserData(userDetail) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ Log.e(TAG, "saveUserData: Success=>") },
                    { throwable -> Log.e(TAG, "saveUserData: Error=>" + throwable.message) })
        )
    }

    override fun deleteUser() {
        compositeDisposable.add(
            Completable.fromAction {
                mAppDatabase.userDao().nukeUserData()
            }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe())
    }

    override fun getUserData(): Single<User> {
        return mAppDatabase.userDao().getUserData()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
    }

}