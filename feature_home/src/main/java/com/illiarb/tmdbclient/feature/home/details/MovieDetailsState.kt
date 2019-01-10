package com.illiarb.tmdbclient.feature.home.details

import com.illiarb.tmdbexplorer.coreui.Cloneable
import com.illiarb.tmdblcient.core.entity.Movie

/**
 * @author ilya-rb on 09.01.19.
 */
data class MovieDetailsState(val isLoading: Boolean, val movie: Movie?) : Cloneable<MovieDetailsState> {

    companion object {
        fun idle() = MovieDetailsState(false, null)
    }

    override fun clone(): MovieDetailsState = copy()
}