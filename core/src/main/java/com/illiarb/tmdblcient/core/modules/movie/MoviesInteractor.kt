package com.illiarb.tmdblcient.core.modules.movie

import com.illiarb.tmdblcient.core.entity.Movie
import io.reactivex.Single

interface MoviesInteractor {

    fun getMoviesByType(type: String): Single<List<Movie>>

    fun getMovieDetails(id: Int, appendToResponse: String): Single<Movie>

}