package com.tmdbclient.servicetmdb.di

import com.illiarb.tmdblcient.core.interactor.GenresInteractor
import com.illiarb.tmdblcient.core.interactor.HomeInteractor
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import com.illiarb.tmdblcient.core.interactor.TrendingInteractor
import com.tmdbclient.servicetmdb.repository.DefaultGenresRepository
import com.tmdbclient.servicetmdb.repository.DefaultMoviesRepository
import com.tmdbclient.servicetmdb.interactor.DefaultGenresInteractor
import com.tmdbclient.servicetmdb.interactor.DefaultHomeInteractor
import com.tmdbclient.servicetmdb.interactor.DefaultMoviesInteractor
import com.tmdbclient.servicetmdb.interactor.DefaultTrendingInteractor
import com.tmdbclient.servicetmdb.repository.GenresRepository
import com.tmdbclient.servicetmdb.repository.MoviesRepository
import dagger.Binds
import dagger.Module

@Module
interface TmdbModule {

    @Binds
    fun bindMoviesInteractor(impl: DefaultMoviesInteractor): MoviesInteractor

    @Binds
    fun bindHomeInteractor(impl: DefaultHomeInteractor): HomeInteractor

    @Binds
    fun bindTrendingInteractor(impl: DefaultTrendingInteractor): TrendingInteractor

    @Binds
    fun bindGenresInteractor(impl: DefaultGenresInteractor): GenresInteractor

    @Binds
    fun bindMoviesRepository(repository: DefaultMoviesRepository): MoviesRepository

    @Binds
    fun bindGenresRepository(repository: DefaultGenresRepository): GenresRepository
}