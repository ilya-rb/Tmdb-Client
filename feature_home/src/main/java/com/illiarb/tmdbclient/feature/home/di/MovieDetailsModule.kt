package com.illiarb.tmdbclient.feature.home.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.feature.home.details.MovieDetailsModel
import com.illiarb.tmdbexplorer.coreui.di.modules.ViewModelKey
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