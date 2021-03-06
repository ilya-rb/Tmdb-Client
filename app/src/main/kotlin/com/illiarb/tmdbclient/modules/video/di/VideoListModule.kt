package com.illiarb.tmdbclient.modules.video.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.modules.video.VideoListViewModel
import com.illiarb.tmdbclient.libs.ui.di.ViewModelKey
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