package com.illiarb.tmdbclient.features.main.di

import androidx.fragment.app.FragmentActivity
import com.illiarb.tmdbclient.features.main.MainActivity
import com.illiarb.tmdbexplorer.coreui.di.ActivityModule
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import dagger.Component

@Component(
    dependencies = [AppProvider::class],
    modules = [
        MainModule::class,
        ActivityModule::class
    ]
)
interface MainComponent {

    companion object {
        fun get(appProvider: AppProvider, activity: FragmentActivity): MainComponent =
            DaggerMainComponent.builder()
                .appProvider(appProvider)
                .activityModule(ActivityModule(activity))
                .build()
    }

    fun inject(activity: MainActivity)
}