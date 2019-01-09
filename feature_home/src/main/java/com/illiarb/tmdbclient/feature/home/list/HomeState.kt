package com.illiarb.tmdbclient.feature.home.list

import com.illiarb.tmdblcient.core.entity.MovieSection
import java.util.Collections

/**
 * @author ilya-rb on 09.01.19.
 */
data class HomeState(val isLoading: Boolean, val movies: List<MovieSection>) {

    companion object {
        fun idle() = HomeState(false, Collections.emptyList())
    }
}