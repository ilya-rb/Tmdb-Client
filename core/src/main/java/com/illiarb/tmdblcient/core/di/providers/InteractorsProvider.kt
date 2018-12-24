package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.modules.movie.MovieDetailsInteractor
import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor

/**
 * @author ilya-rb on 24.12.18.
 */
interface InteractorsProvider {

    fun provideMoviesInteractor(): MoviesInteractor

    fun provideMovieDetailsInteractor(): MovieDetailsInteractor
}