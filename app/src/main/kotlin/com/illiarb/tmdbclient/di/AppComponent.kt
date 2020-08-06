package com.illiarb.tmdbclient.di

import android.app.Application
import com.illiarb.tmdbclient.App
import com.illiarb.tmdbclient.di.modules.AppModule
import com.illiarb.tmdbclient.di.modules.DebugModule
import com.illiarb.tmdbclient.di.modules.InitializersModule
import com.illiarb.tmdbclient.di.modules.NavigationModule
import com.illiarb.tmdbclient.libs.tools.di.ToolsModule
import com.illiarb.tmdbclient.modules.details.di.MovieDetailsComponent
import com.illiarb.tmdbclient.modules.discover.di.DiscoverComponent
import com.illiarb.tmdbclient.modules.discover.filter.di.FilterComponent
import com.illiarb.tmdbclient.modules.home.di.HomeComponent
import com.illiarb.tmdbclient.modules.main.MainComponent
import com.illiarb.tmdbclient.modules.video.di.VideoListComponent
import com.illiarb.tmdbclient.services.analytics.di.AnalyticsModule
import com.illiarb.tmdbclient.services.tmdb.di.TmdbModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
  modules = [
    AppModule::class,
    NavigationModule::class,
    InitializersModule::class,
    ToolsModule::class,
    AnalyticsModule::class,
    TmdbModule::class,
    DebugModule::class
  ]
)
@Singleton
interface AppComponent : AppProvider {

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance app: Application): AppComponent
  }

  fun inject(app: App)
}

interface AppProvider :
  MainComponent.Dependencies,
  HomeComponent.Dependencies,
  VideoListComponent.Dependencies,
  MovieDetailsComponent.Dependencies,
  DiscoverComponent.Dependencies,
  FilterComponent.Dependencies
