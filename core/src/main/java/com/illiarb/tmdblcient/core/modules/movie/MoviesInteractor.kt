package com.illiarb.tmdblcient.core.modules.movie

import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.entity.MovieSection
import com.illiarb.tmdblcient.core.entity.Review
import io.reactivex.Single

interface MoviesInteractor {

    fun getMovieSections(): Single<List<MovieSection>>

    fun getMovieDetails(id: Int, appendToResponse: String): Single<Movie>

    fun getMovieReviews(id: Int): Single<List<Review>>

    fun getMovieFilters(): Single<List<MovieFilter>>
}