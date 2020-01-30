package com.tmdbclient.servicetmdb.di

import com.tmdbclient.servicetmdb.repository.ConfigurationRepository
import com.tmdbclient.servicetmdb.repository.DefaultConfigurationRepository
import com.tmdbclient.servicetmdb.repository.DefaultGenresRepository
import com.tmdbclient.servicetmdb.repository.DefaultMoviesRepository
import com.tmdbclient.servicetmdb.repository.GenresRepository
import com.tmdbclient.servicetmdb.repository.MoviesRepository
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