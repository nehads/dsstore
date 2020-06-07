package com.nehads.friends.di.module

import android.app.Application
import android.content.Context
import com.nehads.friends.BuildConfig
import com.nehads.friends.di.respository_helper.remote.RemoteHelper
import com.nehads.friends.di.respository_helper.remote.RemoteRepo
import com.nehads.friends.di.respository_helper.remote.RetrofitApi
import com.nehads.friends.di.respository_helper.remote.RetrofitHeaderIntercept
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nehads.friends.di.respository_helper.shared_preference.SharedPrefsHelper
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type

@Module
class NetworkModule {
    @Singleton
    @Provides
    internal fun provideGson(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }

    @Provides
    @Singleton
    internal fun provideRetrofitHeaderIntercept(
        application: Application,
        appDataManager: SharedPrefsHelper
    ): RetrofitHeaderIntercept {
        return RetrofitHeaderIntercept(application.applicationContext, appDataManager)
    }

    @Singleton
    @Provides
    internal fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        retrofitHeaderIntercept: RetrofitHeaderIntercept
    ): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(retrofitHeaderIntercept)
        if (BuildConfig.DEBUG) {
            httpClient.addInterceptor(httpLoggingInterceptor)
        }
        return httpClient.build()
    }

    @Provides
    @Singleton
    internal fun provideRetrofitInterface(builder: Retrofit): RetrofitApi {
        return builder.create(RetrofitApi::class.java)
    }

    @Singleton
    @Provides
    internal fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        val retrofit = Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create(gson))
            addConverterFactory(nullOnEmptyConverterFactory)
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            baseUrl(BuildConfig.BASE_URL)
            client(okHttpClient)
        }
        return retrofit.build()
    }


    @Provides
    @Singleton
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    internal fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    /* Remote repository injection is done here.*/
    @Singleton
    @Provides
    internal fun provideRemoteHelper(retrofitApi: RetrofitApi, applicationContext: Context, retrofit: Retrofit): RemoteHelper {
        return RemoteRepo(retrofitApi,applicationContext, retrofit)
    }
}

/*Convert {} to "" - String type - ** */
val nullOnEmptyConverterFactory = object : Converter.Factory() {
    fun converterFactory() = this
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ) = object : Converter<ResponseBody, Any?> {
        val nextResponseBodyConverter =
            retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)

        override fun convert(value: ResponseBody) =
            if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value)
            else ""
    }
}
