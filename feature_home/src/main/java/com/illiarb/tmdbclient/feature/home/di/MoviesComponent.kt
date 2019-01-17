package com.illiarb.tmdbclient.feature.home.di

import com.illiarb.tmdbclient.feature.home.details.ui.MovieDetailsFragment
import com.illiarb.tmdbclient.feature.home.list.ui.HomeFragment
import com.illiarb.tmdbclient.feature.home.photoview.PhotoViewFragment
import com.illiarb.tmdbexplorer.coreui.di.modules.ViewModelModule
import com.illiarb.tmdbexplorer.coreui.di.scope.FragmentScope
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import dagger.Component

@Component(
    dependencies = [AppProvider::class],
    modules = [
        DelegatesModule::class,
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
}