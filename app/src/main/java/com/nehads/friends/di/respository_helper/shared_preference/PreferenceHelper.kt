package com.nehads.friends.di.respository_helper.shared_preference

interface PreferenceHelper {
    fun setAutoLoginFlag()

    fun getAutoLoginFlag(): Boolean
    fun clearPreferenceData()
}