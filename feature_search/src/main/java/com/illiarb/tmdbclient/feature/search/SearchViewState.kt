package com.illiarb.tmdbclient.feature.search

import com.illiarb.tmdblcient.core.entity.Movie
import java.util.Collections

/**
 * @author ilya-rb on 27.12.18.
 */
data class SearchViewState(
    val isSearchRunning: Boolean = false,
    val searchResults: List<Movie> = Collections.emptyList(),
    val error: Throwable? = null
) {

    companion object {
        fun idle() = SearchViewState()
    }
}