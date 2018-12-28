package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.modules.movie.MovieDetailsInteractor
import com.illiarb.tmdblcient.core.modules.movie.HomeInteractor
import com.illiarb.tmdblcient.core.modules.search.SearchInteractor

/**
 * @author ilya-rb on 24.12.18.
 */
interface InteractorsProvider {

    fun provideMoviesInteractor(): HomeInteractor

    fun provideMovieDetailsInteractor(): MovieDetailsInteractor

    fun provideSearchInteractor(): SearchInteractor
}