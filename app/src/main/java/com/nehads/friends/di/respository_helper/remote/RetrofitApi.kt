package com.nehads.friends.di.respository_helper.remote

import com.nehads.friends.model.remote.Episodes
import com.nehads.friends.model.remote.Seasons
import io.reactivex.Single
import retrofit2.http.*

/* Class dealing with API calls */
@JvmSuppressWildcards
interface RetrofitApi {
    companion object {
        private const val DIRECTORY = "3"

        const val TV_PATH = "$DIRECTORY/tv"
        const val SEASON = "season"
    }

    @GET("$TV_PATH/{tv_id}")
    fun getAllSeasons(
        @Path("tv_id") id: Int,
        @Query("api_key") key: String
    ): Single<Seasons>

    @GET("$TV_PATH/{tv_id}/$SEASON/{season_number}")
    fun getEpisodesInSeason(
        @Path("tv_id") id: Int,
        @Path("season_number") seasonNo: Int,
        @Query("api_key") key: String
    ): Single<Episodes>
}