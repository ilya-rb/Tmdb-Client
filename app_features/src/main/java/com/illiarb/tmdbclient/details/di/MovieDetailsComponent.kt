package com.illiarb.tmdbclient.details.di

import androidx.fragment.app.FragmentActivity
import com.illiarb.tmdbclient.details.MovieDetailsFragment
import com.illiarb.tmdbexplorer.coreui.di.ActivityModule
import com.illiarb.tmdbexplorer.coreui.di.ViewModelModule
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import dagger.Component

@Component(
    dependencies = [AppProvider::class],
    modules = [
        MovieDetailsModule::class,
        ViewModelModule::class,
        ActivityModule::class
    ]
)
interface MovieDetailsComponent {

    companion object {
        fun get(
            appProvider: AppProvider,
            activity: FragmentActivity,
            movieId: Int
        ): MovieDetailsComponent =
            DaggerMovieDetailsComponent.builder()
                .appProvider(appProvider)
                .activityModule(ActivityModule(activity))
                .movieDetailsModule(MovieDetailsModule(movieId))
                .build()
    }

    fun inject(fragment: MovieDetailsFragment)
}