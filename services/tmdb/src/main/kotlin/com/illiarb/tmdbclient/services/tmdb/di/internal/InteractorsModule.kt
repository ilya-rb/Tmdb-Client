package com.illiarb.tmdbclient.services.tmdb.di.internal

import com.illiarb.tmdbclient.services.tmdb.interactor.DiscoverInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.FiltersInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.GenresInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.HomeInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.MoviesInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.SearchInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.TrendingInteractor
import com.illiarb.tmdbclient.services.tmdb.internal.interactor.DefaultDiscoverInteractor
import com.illiarb.tmdbclient.services.tmdb.internal.interactor.DefaultFiltersInteractor
import com.illiarb.tmdbclient.services.tmdb.internal.interactor.DefaultGenresInteractor
import com.illiarb.tmdbclient.services.tmdb.internal.interactor.DefaultHomeInteractor
import com.illiarb.tmdbclient.services.tmdb.internal.interactor.DefaultMoviesInteractor
import com.illiarb.tmdbclient.services.tmdb.internal.interactor.DefaultSearchInteractor
import com.illiarb.tmdbclient.services.tmdb.internal.interactor.DefaultTrendingInteractor
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

  @Binds
  fun bindFiltersInteractor(impl: DefaultFiltersInteractor): FiltersInteractor

  @Binds
  fun bindDiscoverInteractor(impl: DefaultDiscoverInteractor): DiscoverInteractor

  @Binds
  fun bindSearchInteractor(impl: DefaultSearchInteractor): SearchInteractor
}