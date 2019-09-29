package com.illiarb.tmdbclient.details.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.details.MovieDetailsViewModel
import com.illiarb.tmdbexplorer.coreui.di.ViewModelKey
import com.illiarb.tmdblcient.core.services.TmdbService
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

/**
 * @author ilya-rb on 08.01.19.
 */
@Module
class MovieDetailsModule(val id: Int) {

    @Provides
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel.DefaultDetailsViewModel::class)
    fun provideMovieDetailsModel(service: TmdbService): ViewModel =
        MovieDetailsViewModel.DefaultDetailsViewModel(service, id)
}