package com.illiarb.tmdbexplorer.coreui.di

import androidx.fragment.app.FragmentActivity
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(
    private val activity: FragmentActivity,
    private val onClickListener: OnClickListener = OnClickListener.DefaultOnClickListener()
) {

    @Provides
    fun provideActivity(): FragmentActivity = activity

    @Provides
    fun provideOnClickListener(): OnClickListener = onClickListener
}