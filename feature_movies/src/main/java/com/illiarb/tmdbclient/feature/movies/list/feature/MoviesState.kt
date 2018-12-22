package com.illiarb.tmdbclient.feature.movies.list.feature

import com.illiarb.tmdblcient.core.entity.MovieSection
import java.util.Collections

/**
 * @author ilya-rb on 22.12.18.
 */
data class MoviesState(
    val isLoading: Boolean = false,
    val movies: List<MovieSection> = Collections.emptyList(),
    val error: Throwable? = null
) {
    companion object {
        fun idle() = MoviesState(false)
    }
}