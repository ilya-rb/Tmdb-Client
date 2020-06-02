package com.illiarb.tmdbclient.di

import android.app.Application
import com.illiarb.tmdbclient.App
import com.illiarb.tmdbclient.di.modules.AppModule
import com.illiarb.tmdbclient.di.modules.InitializersModule
import com.illiarb.tmdbclient.di.modules.NavigationModule
import com.illiarb.tmdbclient.libs.tools.di.ToolsProvider
import com.illiarb.tmdbclient.modules.details.di.MovieDetailsComponent
import com.illiarb.tmdbclient.modules.discover.di.DiscoverComponent
import com.illiarb.tmdbclient.modules.discover.filter.di.FilterComponent
import com.illiarb.tmdbclient.modules.home.di.HomeComponent
import com.illiarb.tmdbclient.modules.main.MainComponent
import com.illiarb.tmdbclient.modules.video.di.VideoListComponent
import com.illiarb.tmdbclient.services.analytics.di.AnalyticsProvider
import com.illiarb.tmdbclient.services.tmdb.di.TmdbProvider
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
  dependencies = [
    ToolsProvider::class,
    TmdbProvider::class,
    AnalyticsProvider::class
  ],
  modules = [
    AppModule::class,
    NavigationModule::class,
    InitializersModule::class
  ]
)
@Singleton
interface AppComponent : AppProvider {

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun application(app: Application): Builder
    fun toolsProvider(provider: ToolsProvider): Builder
    fun tmdbProvider(provider: TmdbProvider): Builder
    fun analyticsProvider(provider: AnalyticsProvider): Builder
    fun appModule(module: AppModule): Builder
    fun build(): AppComponent
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