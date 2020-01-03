package com.tmdbclient.service_tmdb.di

import com.illiarb.tmdblcient.core.services.TmdbService
import com.tmdbclient.service_tmdb.DefaultGenresRepository
import com.tmdbclient.service_tmdb.DefaultMoviesRepository
import com.tmdbclient.service_tmdb.DefaultTmdbService
import com.tmdbclient.service_tmdb.GenresRepository
import com.tmdbclient.service_tmdb.MoviesRepository
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