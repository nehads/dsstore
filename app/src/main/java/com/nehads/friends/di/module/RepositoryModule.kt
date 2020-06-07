package com.nehads.friends.di.module

import android.content.Context
import androidx.room.Room
import com.nehads.friends.BuildConfig
import com.nehads.friends.di.repository.AppDataManager
import com.nehads.friends.di.repository.DataRepository
import com.nehads.friends.di.respository_helper.local.db.AppDatabase
import com.nehads.friends.di.respository_helper.local.db.AppDbHelper
import com.nehads.friends.di.respository_helper.local.db.DatabaseHelper
import com.nehads.friends.di.respository_helper.remote.RemoteHelper
import com.nehads.friends.di.respository_helper.shared_preference.PreferenceHelper
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    internal fun appDatabase(applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java!!,
            BuildConfig.APP_DB
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    internal fun provideDbHelper(appDbHelper: AppDbHelper): DatabaseHelper {
        return appDbHelper
    }

    @Singleton
    @Provides
    internal fun dataRepository(
        databaseHelper: DatabaseHelper,
        remoteHelper: RemoteHelper,
        preferencesHelper: PreferenceHelper,
        compositeDisposable: CompositeDisposable
    ): DataRepository {
        return AppDataManager(
            databaseHelper,
            remoteHelper,
            preferencesHelper,
            compositeDisposable
        )
    }
}