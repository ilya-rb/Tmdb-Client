package com.illiarb.tmdbclient.storage.di

import com.illiarb.tmdbclient.storage.network.api.movie.MovieRemoteDataSource
import com.illiarb.tmdblcient.core.modules.movie.MovieDataSource
import dagger.Binds
import dagger.Module

/**
 * @author ilya-rb on 25.10.18.
 */
@Module
interface DataSourceModule {

    @Binds
    fun bindMovieRemoteDataSource(impl: MovieRemoteDataSource): MovieDataSource
}