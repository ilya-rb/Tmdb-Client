package com.illiarb.tmdbclient.home.di

import com.illiarb.tmdbclient.home.HomeFragment
import com.illiarb.tmdbexplorer.coreui.di.ViewModelModule
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import dagger.Component

@Component(
    dependencies = [AppProvider::class],
    modules = [
        HomeModule::class,
        ViewModelModule::class
    ]
)
interface HomeComponent {

    companion object {
        fun get(appProvider: AppProvider): HomeComponent =
            DaggerHomeComponent.builder()
                .appProvider(appProvider)
                .build()
    }

    fun inject(fragment: HomeFragment)
}