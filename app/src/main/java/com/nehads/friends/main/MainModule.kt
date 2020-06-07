package com.nehads.friends.main

import com.nehads.friends.di.scope.FragmentScoped
import com.nehads.friends.main.history.HistoryFragment
import com.nehads.friends.main.login.LoginFragment
import com.nehads.friends.main.episodes.EpisodesFragment
import com.nehads.friends.main.landing.LandingFragment
import com.nehads.friends.main.profile.ProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Contains all fragments connected to MainActivity*/
@Module
abstract class MainModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun loginFragment(): LoginFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun landingFragment(): LandingFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun episodesFragment(): EpisodesFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun profileFragment(): ProfileFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun historyFragment(): HistoryFragment
}