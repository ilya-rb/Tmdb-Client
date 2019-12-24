package com.illiarb.tmdbclient.details.di

import com.illiarb.tmdbclient.details.MovieDetailsFragment
import com.illiarb.tmdbexplorer.coreui.di.ViewModelModule
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import dagger.Component

@Component(
    dependencies = [AppProvider::class],
    modules = [MovieDetailsModule::class, ViewModelModule::class]
)
interface MovieDetailsComponent {

    companion object {
        fun get(appProvider: AppProvider, movieId: Int): MovieDetailsComponent =
            DaggerMovieDetailsComponent.builder()
                .appProvider(appProvider)
                .movieDetailsModule(MovieDetailsModule(movieId))
                .build()
    }

    fun inject(fragment: MovieDetailsFragment)
}