package com.illiarb.tmdbclient.home.di

import androidx.fragment.app.FragmentActivity
import com.illiarb.tmdbclient.home.HomeFragment
import com.illiarb.tmdbexplorer.coreui.di.ActivityModule
import com.illiarb.tmdbexplorer.coreui.di.ViewModelModule
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import dagger.Component

@Component(
    dependencies = [AppProvider::class],
    modules = [
        HomeModule::class,
        ViewModelModule::class,
        ActivityModule::class
    ]
)
interface HomeComponent {

    companion object {
        fun get(appProvider: AppProvider, activity: FragmentActivity): HomeComponent =
            DaggerHomeComponent.builder()
                .appProvider(appProvider)
                .activityModule(ActivityModule(activity))
                .build()
    }

    fun inject(fragment: HomeFragment)
}