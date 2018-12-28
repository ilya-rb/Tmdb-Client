package com.illiarb.tmdbclient.feature.home.di

import com.illiarb.tmdbclient.feature.home.details.ui.MovieDetailsFragment
import com.illiarb.tmdbclient.feature.home.list.ui.MoviesFragment
import com.illiarb.tmdbexplorer.coreui.di.UiEventsModule
import com.illiarb.tmdbexplorer.coreui.di.scope.FragmentScope
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import dagger.Component

@Component(
    dependencies = [AppProvider::class],
    modules = [
        UiEventsModule::class,
        DelegatesModule::class
    ]
)
@FragmentScope
interface MoviesComponent {

    companion object {
        fun get(appProvider: AppProvider): MoviesComponent =
            DaggerMoviesComponent.builder()
                .appProvider(appProvider)
                .build()
    }

    fun inject(fragment: MoviesFragment)

    fun inject(fragment: MovieDetailsFragment)
}