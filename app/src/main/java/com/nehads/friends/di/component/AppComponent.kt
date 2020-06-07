package com.nehads.friends.di.component

import android.app.Application
import com.nehads.friends.DIApplication
import com.nehads.friends.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ApplicationModule::class, NetworkModule::class, SharedPreferenceModule::class, RepositoryModule::class, ViewModelModule::class, ActivityBindingModule::class, AndroidSupportInjectionModule::class]
)

interface AppComponent : AndroidInjector<DIApplication> {

    override fun inject(app: DIApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}