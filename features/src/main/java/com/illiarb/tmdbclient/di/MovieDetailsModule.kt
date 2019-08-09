package com.illiarb.tmdbclient.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.details.presentation.MovieDetailsModel
import com.illiarb.tmdbexplorer.coreui.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author ilya-rb on 08.01.19.
 */
@Module
interface MovieDetailsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsModel::class)
    fun bindMovieDetailsModel(model: MovieDetailsModel): ViewModel
}