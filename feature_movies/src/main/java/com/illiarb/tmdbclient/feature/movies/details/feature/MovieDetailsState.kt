package com.illiarb.tmdbclient.feature.movies.details.feature

import com.illiarb.tmdblcient.core.entity.Movie

/**
 * @author ilya-rb on 22.12.18.
 */
data class MovieDetailsState(
    val isLoading: Boolean = false,
    val movie: Movie? = null,
    val error: Throwable? = null
) {

    companion object {
        fun idle() = MovieDetailsState()
    }
}