package com.illiarb.tmdbclient.video.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.video.DefaultVideoListModel
import com.illiarb.tmdbexplorer.coreui.di.ViewModelKey
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class VideoListModule(private val movieId: Int) {

    @Provides
    @IntoMap
    @ViewModelKey(DefaultVideoListModel::class)
    fun provideVideoListModel(interactor: MoviesInteractor): ViewModel {
        return DefaultVideoListModel(movieId, interactor)
    }
}