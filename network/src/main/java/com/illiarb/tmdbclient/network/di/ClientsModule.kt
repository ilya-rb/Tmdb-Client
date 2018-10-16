package com.illiarb.tmdbclient.network.di

import com.illiarb.tmdbclient.network.api.movie.MoviesApiImpl
import com.illiarb.tmdblcient.core.modules.movie.MoviesApi
import dagger.Binds
import dagger.Module

@Module
interface ClientsModule {

    @Binds
    fun bindMoviesApi(impl: MoviesApiImpl): MoviesApi

}