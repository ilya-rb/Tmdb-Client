package com.illiarb.tmdbclient.details.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.details.MovieDetailsModel
import com.illiarb.tmdbexplorer.coreui.di.ViewModelKey
import com.illiarb.tmdblcient.core.services.TmdbService
import dagger.Binds
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
    @ViewModelKey(MovieDetailsModel::class)
    fun provideMovieDetailsModel(service: TmdbService): ViewModel = MovieDetailsModel(service, id)
}