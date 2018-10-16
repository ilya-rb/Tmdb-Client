package com.illiarb.tmdbexplorerdi.providers

import com.illiarb.tmdblcient.core.modules.movie.MoviesApi

interface NetworkProvider {

    fun provideMoviesApi(): MoviesApi

}