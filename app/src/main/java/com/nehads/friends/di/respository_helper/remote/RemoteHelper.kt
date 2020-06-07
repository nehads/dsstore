package com.nehads.friends.di.respository_helper.remote

import com.nehads.friends.model.remote.Episodes
import com.nehads.friends.model.remote.ResponseModel
import com.nehads.friends.model.remote.Seasons
import io.reactivex.Single

/* An Interface that a list of APIs used */
interface RemoteHelper {
    fun getAllSeasons(tvId: Int): Single<ResponseModel<Seasons>>

    fun getEpisodesInSeason(tvId: Int, seasonNo: Int): Single<ResponseModel<Episodes>>
}