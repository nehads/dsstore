package com.nehads.friends.di.module

import com.nehads.friends.di.scope.ActivityScoped
import com.nehads.friends.main.MainActivity
import com.nehads.friends.main.MainModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun provideMainActivity(): MainActivity

}