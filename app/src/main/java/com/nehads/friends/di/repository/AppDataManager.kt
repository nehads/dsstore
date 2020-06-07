package com.nehads.friends.di.repository

import com.nehads.friends.di.respository_helper.local.db.DatabaseHelper
import com.nehads.friends.di.respository_helper.local.db.entity.User
import com.nehads.friends.di.respository_helper.remote.RemoteHelper
import com.nehads.friends.di.respository_helper.shared_preference.PreferenceHelper
import com.nehads.friends.model.remote.Episodes
import com.nehads.friends.model.remote.ResponseModel
import com.nehads.friends.model.remote.Seasons
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/* The main data store that connects to either the remote calls or local preference functions */
@Singleton
class AppDataManager @Inject
constructor(
    private val mDbHelper: DatabaseHelper,
    private val mRemoteHelper: RemoteHelper,
    private val mPrefHelper: PreferenceHelper,
    private val compositeDisposable: CompositeDisposable
) : DataRepository {

    override fun getAllSeasons(tvId: Int): Single<ResponseModel<Seasons>> {
        return mRemoteHelper.getAllSeasons(tvId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getEpisodesInSeason(tvId: Int, seasonNo: Int): Single<ResponseModel<Episodes>> {
        return mRemoteHelper.getEpisodesInSeason(tvId, seasonNo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun deleteUser() {
        mDbHelper.deleteUser()
    }

    override fun saveUserData(userDetail: User) {
        mDbHelper.saveUserData(userDetail)
    }

    override fun getUserData(): Single<User> {
        return mDbHelper.getUserData()
    }

    override fun setAutoLoginFlag() {
        return mPrefHelper.setAutoLoginFlag()
    }

    override fun getAutoLoginFlag(): Boolean {
        return mPrefHelper.getAutoLoginFlag()
    }

    override fun clearPreferenceData() {
        return mPrefHelper.clearPreferenceData()
    }

    /* function to delete all user related data on logout */
    override fun nukeTheUser(): Single<Boolean> {
        return Single.fromCallable {
            clearPreferenceData()
            deleteUser()
            true
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}