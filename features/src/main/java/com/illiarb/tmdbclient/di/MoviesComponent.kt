package com.illiarb.tmdbclient.di

import com.illiarb.tmdbclient.details.ui.MovieDetailsFragment
import com.illiarb.tmdbclient.details.ui.reviews.ReviewsFragment
import com.illiarb.tmdbclient.home.ui.HomeFragment
import com.illiarb.tmdbclient.photoview.PhotoViewFragment
import com.illiarb.tmdbexplorer.coreui.di.ViewModelModule
import com.illiarb.tmdbexplorer.coreui.di.scope.FragmentScope
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import dagger.Component

@Component(
    dependencies = [AppProvider::class],
    modules = [
        HomeModule::class,
        MovieDetailsModule::class,
        ViewModelModule::class
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

    fun inject(fragment: HomeFragment)

    fun inject(fragment: MovieDetailsFragment)

    fun inject(fragment: PhotoViewFragment)

    fun inject(fragment: ReviewsFragment)
}