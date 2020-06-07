package com.nehads.friends.di.respository_helper.remote

import android.content.Context
import android.util.Log
import com.nehads.friends.di.respository_helper.shared_preference.PreferenceHelper
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RetrofitHeaderIntercept(
    private val appContext: Context,
    private val dataRepository: PreferenceHelper
) : Interceptor {

    companion object {
        private val TAG = RetrofitHeaderIntercept::class.java.canonicalName
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        request = request.newBuilder()
            .build()

        val response = chain.proceed(request)
        Log.e(TAG, "intercept: ResponseModel Code => " + response.code())

        return response
    }
}
