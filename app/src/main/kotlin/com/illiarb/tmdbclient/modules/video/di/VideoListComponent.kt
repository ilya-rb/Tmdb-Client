package com.illiarb.tmdbclient.modules.video.di

import com.illiarb.tmdbclient.libs.ui.di.ViewModelModule
import com.illiarb.tmdbclient.modules.video.VideoListFragment
import com.illiarb.tmdbclient.navigation.Router
import com.illiarb.tmdbclient.services.tmdb.interactor.MoviesInteractor
import dagger.BindsInstance
import dagger.Component

@Component(
  dependencies = [VideoListComponent.Dependencies::class],
  modules = [
    ViewModelModule::class,
    VideoListModule::class
  ]
)
interface VideoListComponent {

  interface Dependencies {
    fun moviesInteractor(): MoviesInteractor
    fun router(): Router
  }

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance movieId: Int, dependencies: Dependencies): VideoListComponent
  }

  fun inject(fragment: VideoListFragment)
}