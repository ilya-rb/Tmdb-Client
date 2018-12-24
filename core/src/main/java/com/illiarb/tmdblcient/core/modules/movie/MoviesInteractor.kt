package com.illiarb.tmdblcient.core.modules.movie

import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.MovieSection
import io.reactivex.Single

interface MoviesInteractor {

    fun getMovieSections(): Single<List<MovieSection>>

    fun onMovieSelected(movie: Movie)
}