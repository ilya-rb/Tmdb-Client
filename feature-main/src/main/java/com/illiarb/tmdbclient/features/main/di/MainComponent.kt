package com.illiarb.tmdbclient.features.main.di

import com.illiarb.tmdbclient.features.main.MainActivity
import com.illiarb.tmdbexplorer.coreui.di.ViewModelModule
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import dagger.Component

@Component(
    dependencies = [AppProvider::class],
    modules = [
        ViewModelModule::class,
        MainModule::class
    ]
)
interface MainComponent {

    companion object {
        fun get(appProvider: AppProvider): MainComponent =
            DaggerMainComponent.builder()
                .appProvider(appProvider)
                .build()
    }

    fun inject(activity: MainActivity)
}