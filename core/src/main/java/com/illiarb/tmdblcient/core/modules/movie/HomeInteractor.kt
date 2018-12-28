package com.illiarb.tmdblcient.core.modules.movie

import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.entity.MovieSection
import io.reactivex.Single

interface HomeInteractor {

    fun getMovieSections(): Single<List<MovieSection>>

    fun getMovieFilters(): Single<List<MovieFilter>>

    fun onMovieSelected(movie: Movie)

    fun onSearchClicked()

    fun onAccountClicked()
}