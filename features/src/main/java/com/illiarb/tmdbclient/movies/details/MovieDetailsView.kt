package com.illiarb.tmdbclient.movies.details

import com.illiarb.tmdblcient.core.domain.entity.Movie
import kotlinx.coroutines.CoroutineScope

interface MovieDetailsView : CoroutineScope {

    fun movie(movie: Movie)
}