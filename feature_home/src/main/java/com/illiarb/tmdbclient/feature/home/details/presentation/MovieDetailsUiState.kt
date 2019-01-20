package com.illiarb.tmdbclient.feature.home.details.presentation

import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.util.Cloneable

/**
 * @author ilya-rb on 09.01.19.
 */
data class MovieDetailsUiState(
    val isLoading: Boolean,
    val movie: Movie?
) : Cloneable<MovieDetailsUiState> {

    companion object {
        fun idle() = MovieDetailsUiState(false, null)
    }

    override fun clone(): MovieDetailsUiState = copy()
}