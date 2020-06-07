package com.nehads.friends.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nehads.friends.di.factories.ViewModelProviderFactory
import com.nehads.friends.di.scope.ViewModelKey
import com.nehads.friends.main.landing.LandingViewModel
import com.nehads.friends.main.login.LoginViewModel
import com.nehads.friends.main.profile.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindFactory(viewModelProviderFactory: ViewModelProviderFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindsLoginViewModel(landingVM: LoginViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(LandingViewModel::class)
    abstract fun bindsLandingViewModel(landingVM: LandingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindsProfileViewModel(profileVM: ProfileViewModel): ViewModel
}