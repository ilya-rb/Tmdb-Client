package com.illiarb.tmdbexplorer.coreui.di

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import dagger.Module
import dagger.Provides

/**
 * @author ilya-rb on 05.11.18.
 */
@Module
class ActivityModule(private val activity: FragmentActivity) {

    @Provides
    fun provideActivity(): FragmentActivity = activity

    @Provides
    fun provideFragmentManager(): FragmentManager = activity.supportFragmentManager
}