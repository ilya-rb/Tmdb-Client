package com.illiarb.tmdbclient.di

import androidx.fragment.app.FragmentActivity
import com.illiarb.tmdbclient.movies.details.MovieDetailsFragment
import com.illiarb.tmdbclient.movies.home.HomeFragment
import com.illiarb.tmdbexplorer.coreui.di.ActivityModule
import com.illiarb.tmdbexplorer.coreui.di.ViewModelModule
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import dagger.Component

@Component(
    dependencies = [AppProvider::class],
    modules = [
        HomeModule::class,
        MovieDetailsModule::class,
        ViewModelModule::class,
        ActivityModule::class
    ]
)
interface MoviesComponent {

    companion object {
        fun get(appProvider: AppProvider, activity: FragmentActivity): MoviesComponent =
            DaggerMoviesComponent.builder()
                .appProvider(appProvider)
                .activityModule(ActivityModule(activity))
                .build()
    }

    fun inject(fragment: HomeFragment)

    fun inject(fragment: MovieDetailsFragment)
}