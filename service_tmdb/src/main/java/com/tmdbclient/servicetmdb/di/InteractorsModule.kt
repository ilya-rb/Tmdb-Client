package com.tmdbclient.servicetmdb.di

import com.illiarb.tmdblcient.core.interactor.GenresInteractor
import com.illiarb.tmdblcient.core.interactor.HomeInteractor
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import com.illiarb.tmdblcient.core.interactor.TrendingInteractor
import com.tmdbclient.servicetmdb.interactor.DefaultGenresInteractor
import com.tmdbclient.servicetmdb.interactor.DefaultHomeInteractor
import com.tmdbclient.servicetmdb.interactor.DefaultMoviesInteractor
import com.tmdbclient.servicetmdb.interactor.DefaultTrendingInteractor
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