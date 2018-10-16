package com.illiarb.tmdbexplorerdi.providers

import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor

interface InteractorsProvider {

    fun provideMoviesInteractor(): MoviesInteractor

}