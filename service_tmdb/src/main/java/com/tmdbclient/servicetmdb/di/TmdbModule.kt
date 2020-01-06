package com.tmdbclient.servicetmdb.di

import com.illiarb.tmdblcient.core.services.TmdbService
import com.tmdbclient.servicetmdb.DefaultGenresRepository
import com.tmdbclient.servicetmdb.DefaultMoviesRepository
import com.tmdbclient.servicetmdb.DefaultTmdbService
import com.tmdbclient.servicetmdb.GenresRepository
import com.tmdbclient.servicetmdb.MoviesRepository
import dagger.Binds
import dagger.Module

@Module
interface TmdbModule {

    @Binds
    fun bindTmdbService(impl: DefaultTmdbService): TmdbService

    @Binds
    fun bindMoviesRepository(repository: DefaultMoviesRepository): MoviesRepository

    @Binds
    fun bindGenresRepository(repository: DefaultGenresRepository): GenresRepository
}