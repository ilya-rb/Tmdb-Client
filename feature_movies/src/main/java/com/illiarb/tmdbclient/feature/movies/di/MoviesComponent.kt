package com.illiarb.tmdbclient.feature.movies.di

import com.illiarb.tmdbclient.feature.movies.details.ui.MovieDetailsFragment
import com.illiarb.tmdbclient.feature.movies.list.ui.MoviesFragment
import com.illiarb.tmdbexplorer.coreui.di.UiEventsModule
import com.illiarb.tmdbexplorer.coreui.di.scope.FragmentScope
import com.illiarb.tmdbexplorerdi.providers.AppProvider
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