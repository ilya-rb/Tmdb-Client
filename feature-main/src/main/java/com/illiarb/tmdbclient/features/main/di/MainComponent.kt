package com.illiarb.tmdbclient.features.main.di

import androidx.fragment.app.FragmentActivity
import com.illiarb.tmdbclient.feature.movies.di.MoviesComponent
import com.illiarb.tmdbclient.features.main.MainActivity
import com.illiarb.tmdbexplorer.coreui.di.ViewModelModule
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdbexplorerdi.providers.StartScreenActionProvider
import dagger.Component

@Component(
    dependencies = [AppProvider::class, StartScreenActionProvider::class],
    modules = [ViewModelModule::class, MainModule::class]
)
interface MainComponent {

    companion object {
        fun get(appProvider: AppProvider, activity: FragmentActivity): MainComponent =
            DaggerMainComponent.builder()
                .appProvider(appProvider)
                .startScreenActionProvider(MoviesComponent.get(appProvider, activity))
                .build()
    }

    fun inject(activity: MainActivity)
}