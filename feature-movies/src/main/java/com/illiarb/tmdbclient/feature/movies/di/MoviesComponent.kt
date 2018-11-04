package com.illiarb.tmdbclient.feature.movies.di

import androidx.fragment.app.FragmentActivity
import com.illiarb.tmdbclient.feature.movies.details.MovieDetailsFragment
import com.illiarb.tmdbclient.feature.movies.details.photos.MovieDetailsPhotosFragment
import com.illiarb.tmdbclient.feature.movies.details.reviews.MovieDetailsReviewsFragment
import com.illiarb.tmdbclient.feature.movies.list.MoviesFragment
import com.illiarb.tmdbexplorer.coreui.di.ViewModelModule
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import dagger.Component

@Component(
    dependencies = [AppProvider::class],
    modules = [
        ViewModelModule::class,
        MoviesModule::class,
        ActivityModule::class
    ]
)
interface MoviesComponent {

    companion object {
        fun get(appDepsProvider: AppProvider, activity: FragmentActivity): MoviesComponent =
            DaggerMoviesComponent.builder()
                .appProvider(appDepsProvider)
                .activityModule(ActivityModule(activity))
                .build()
    }

    fun inject(fragment: MoviesFragment)

    fun inject(fragment: MovieDetailsFragment)

    fun inject(fragment: MovieDetailsReviewsFragment)

    fun inject(fragment: MovieDetailsPhotosFragment)
}