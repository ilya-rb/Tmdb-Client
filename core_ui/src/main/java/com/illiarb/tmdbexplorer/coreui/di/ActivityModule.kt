package com.illiarb.tmdbexplorer.coreui.di

import androidx.fragment.app.FragmentActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: FragmentActivity) {

    @Provides
    fun provideActivity(): FragmentActivity = activity
}