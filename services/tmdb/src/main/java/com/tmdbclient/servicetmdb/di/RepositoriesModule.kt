package com.tmdbclient.servicetmdb.di

import com.tmdbclient.servicetmdb.internal.repository.ConfigurationRepository
import com.tmdbclient.servicetmdb.internal.repository.DefaultConfigurationRepository
import com.tmdbclient.servicetmdb.internal.repository.DefaultGenresRepository
import com.tmdbclient.servicetmdb.internal.repository.DefaultMoviesRepository
import com.tmdbclient.servicetmdb.internal.repository.GenresRepository
import com.tmdbclient.servicetmdb.internal.repository.MoviesRepository
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