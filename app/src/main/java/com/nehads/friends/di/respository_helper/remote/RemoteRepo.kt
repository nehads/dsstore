package com.nehads.friends.di.respository_helper.remote

import android.content.Context
import com.google.gson.Gson
import com.nehads.friends.R
import com.nehads.friends.model.remote.Episodes
import com.nehads.friends.model.remote.ErrorModel
import com.nehads.friends.model.remote.ResponseModel
import com.nehads.friends.model.remote.Seasons
import com.nehads.friends.util.Constants
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

/* A repository that connect the API to the API calls, sends data to the API class */

@Singleton
class RemoteRepo @Inject
constructor(
    private val mRetrofitApi: RetrofitApi,
    private val applicationContext: Context, private val retrofit: Retrofit
) : RemoteHelper {

    override fun getAllSeasons(tvId: Int): Single<ResponseModel<Seasons>> {
        return mRetrofitApi.getAllSeasons(tvId, getString(R.string.api_key))
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { Single.just(ResponseModel(successData = it)) }
            .onErrorResumeNext { handleOnError(it) }
    }

    override fun getEpisodesInSeason(tvId: Int, seasonNo: Int): Single<ResponseModel<Episodes>> {
        return mRetrofitApi.getEpisodesInSeason(tvId, seasonNo, getString(R.string.api_key))
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { Single.just(ResponseModel(successData = it)) }
            .onErrorResumeNext { handleOnError(it) }
    }


    /* Error handler that checks for any error returned from API and converts to Error Model to display error msgs */
    private fun <T> handleOnError(it: Throwable): Single<ResponseModel<T>> {
        var errorM: ErrorModel? = null

        if (it is HttpException) {
            if (it.code() == Constants.INTERNAL_SERVER_ERROR)
                errorM = ErrorModel(
                    103,
                    getString(R.string.internal_server_error)
                )
            else {
                val body = it.response()?.errorBody()
                val adapter = Gson().getAdapter(ErrorModel::class.java)
                try {
                    errorM = adapter.fromJson(body?.string())

                } catch (e: IOException) {
                    errorM =
                        ErrorModel(101, getString(R.string.error_occured))
                    e.printStackTrace()
                }
            }
        } else if (it is UnknownHostException || it is SocketTimeoutException) {
            errorM = ErrorModel(
                Constants.NO_INTERNET,
                getString(R.string.could_not_connect_internet)
            )
        } else if (it is ConnectException) {
            errorM =
                ErrorModel(102, getString(R.string.could_not_connect_server))
        } else {
            errorM = ErrorModel(105, getString(R.string.error_occured))
        }
        return Single.just(
            ResponseModel(
                error = errorM
            )
        )
    }

    private fun getString(message: Int): String {
        return applicationContext.getString(message)
    }

}