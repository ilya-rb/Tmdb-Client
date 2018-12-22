package com.illiarb.tmdblcient.core.modules.movie

import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.entity.MovieSection
import io.reactivex.Single

interface MoviesInteractor {

    fun getMovieSections(): Single<List<MovieSection>>

    fun getMovieDetails(id: Int): Single<Movie>

    fun getMovieFilters(): Single<List<MovieFilter>>

    fun onMovieSelected(id: Int, title: String, posterPath: String?)
}