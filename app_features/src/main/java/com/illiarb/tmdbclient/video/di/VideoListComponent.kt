package com.illiarb.tmdbclient.video.di

import com.illiarb.tmdbclient.video.VideoListFragment
import com.illiarb.tmdbexplorer.coreui.di.ViewModelModule
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import dagger.Component

@Component(
  dependencies = [AppProvider::class],
  modules = [
    ViewModelModule::class,
    VideoListModule::class
  ]
)
interface VideoListComponent {

  companion object {

    fun get(appProvider: AppProvider, movieId: Int): VideoListComponent {
      return DaggerVideoListComponent.builder()
        .appProvider(appProvider)
        .videoListModule(VideoListModule(movieId))
        .build()
    }
  }

  fun inject(fragment: VideoListFragment)
}