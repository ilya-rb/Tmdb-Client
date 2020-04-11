package com.illiarb.tmdbclient.ui.video.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.ui.video.VideoListViewModel
import com.illiarb.tmdbexplorer.coreui.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface VideoListModule {

  @Binds
  @IntoMap
  @ViewModelKey(VideoListViewModel::class)
  fun bindVideoListViewModel(viewModel: VideoListViewModel): ViewModel
}