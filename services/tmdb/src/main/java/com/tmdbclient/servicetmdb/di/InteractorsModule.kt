package com.tmdbclient.servicetmdb.di

import com.tmdbclient.servicetmdb.interactor.GenresInteractor
import com.tmdbclient.servicetmdb.interactor.HomeInteractor
import com.tmdbclient.servicetmdb.interactor.MoviesInteractor
import com.tmdbclient.servicetmdb.interactor.TrendingInteractor
import com.tmdbclient.servicetmdb.internal.interactor.DefaultGenresInteractor
import com.tmdbclient.servicetmdb.internal.interactor.DefaultHomeInteractor
import com.tmdbclient.servicetmdb.internal.interactor.DefaultMoviesInteractor
import com.tmdbclient.servicetmdb.internal.interactor.DefaultTrendingInteractor
import dagger.Binds
import dagger.Module

@Module
internal interface InteractorsModule {

  @Binds
  fun bindMoviesInteractor(impl: DefaultMoviesInteractor): MoviesInteractor

  @Binds
  fun bindHomeInteractor(impl: DefaultHomeInteractor): HomeInteractor

  @Binds
  fun bindTrendingInteractor(impl: DefaultTrendingInteractor): TrendingInteractor

  @Binds
  fun bindGenresInteractor(impl: DefaultGenresInteractor): GenresInteractor
}