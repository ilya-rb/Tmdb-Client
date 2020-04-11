package com.illiarb.tmdbclient.libs.ui.video.di

import com.illiarb.tmdbclient.libs.ui.video.VideoListFragment
import com.illiarb.tmdbclient.libs.ui.di.ViewModelModule
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
  }

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun movieId(movieId: Int): Builder
    fun dependencies(dependencies: Dependencies): Builder
    fun build(): VideoListComponent
  }

  fun inject(fragment: VideoListFragment)
}