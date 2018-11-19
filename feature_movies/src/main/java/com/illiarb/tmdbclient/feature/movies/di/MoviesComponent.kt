package com.illiarb.tmdbclient.feature.movies.di

import com.illiarb.tmdbclient.feature.movies.details.MovieDetailsFragment
import com.illiarb.tmdbclient.feature.movies.list.MoviesFragment
import com.illiarb.tmdbexplorer.coreui.di.UiEventsModule
import com.illiarb.tmdbexplorer.coreui.di.ViewModelModule
import com.illiarb.tmdbexplorer.coreui.di.scope.FragmentScope
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import dagger.Component

@Component(
    dependencies = [AppProvider::class],
    modules = [
        ViewModelModule::class,
        UiEventsModule::class,
        MoviesModule::class,
        DelegatesModule::class
    ]
)
@FragmentScope
interface MoviesComponent {

    companion object {
        fun get(appDepsProvider: AppProvider): MoviesComponent =
            DaggerMoviesComponent.builder()
                .appProvider(appDepsProvider)
                .build()
    }

    fun inject(fragment: MoviesFragment)

    fun inject(fragment: MovieDetailsFragment)
}