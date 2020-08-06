package com.illiarb.tmdbclient.services.tmdb.di

import com.illiarb.tmdbclient.services.tmdb.di.internal.ApiModule
import com.illiarb.tmdbclient.services.tmdb.di.internal.ConfigurationModule
import com.illiarb.tmdbclient.services.tmdb.di.internal.DatabaseModule
import com.illiarb.tmdbclient.services.tmdb.di.internal.InteractorsModule
import com.illiarb.tmdbclient.services.tmdb.di.internal.NetworkModule
import com.illiarb.tmdbclient.services.tmdb.di.internal.RepositoriesModule
import dagger.Module

@Module(
  includes = [
    ApiModule::class,
    NetworkModule::class,
    RepositoriesModule::class,
    DatabaseModule::class,
    InteractorsModule::class,
    ConfigurationModule::class
  ]
)
object TmdbModule