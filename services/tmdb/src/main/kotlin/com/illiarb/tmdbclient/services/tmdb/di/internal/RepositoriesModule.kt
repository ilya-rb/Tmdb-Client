package com.illiarb.tmdbclient.services.tmdb.di.internal

import com.illiarb.tmdbclient.services.tmdb.internal.repository.ConfigurationRepository
import com.illiarb.tmdbclient.services.tmdb.internal.repository.DefaultConfigurationRepository
import com.illiarb.tmdbclient.services.tmdb.internal.repository.DefaultGenresRepository
import com.illiarb.tmdbclient.services.tmdb.internal.repository.DefaultMoviesRepository
import com.illiarb.tmdbclient.services.tmdb.internal.repository.GenresRepository
import com.illiarb.tmdbclient.services.tmdb.internal.repository.MoviesRepository
import dagger.Binds
import dagger.Module

@Module
internal interface RepositoriesModule {

  @Binds
  fun bindMoviesRepository(impl: DefaultMoviesRepository): MoviesRepository

  @Binds
  fun bindGenresRepository(impl: DefaultGenresRepository): GenresRepository

  @Binds
  fun bindConfigurationRepository(impl: DefaultConfigurationRepository): ConfigurationRepository
}