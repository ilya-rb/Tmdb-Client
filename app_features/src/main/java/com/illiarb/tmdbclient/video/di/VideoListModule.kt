package com.illiarb.tmdbclient.video.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.video.VideoListViewModel
import com.illiarb.tmdbexplorer.coreui.di.ViewModelKey
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class VideoListModule(private val movieId: Int) {

  @Provides
  @IntoMap
  @ViewModelKey(VideoListViewModel::class)
  fun provideVideoListViewModel(interactor: MoviesInteractor): ViewModel {
    return VideoListViewModel(movieId, interactor)
  }
}