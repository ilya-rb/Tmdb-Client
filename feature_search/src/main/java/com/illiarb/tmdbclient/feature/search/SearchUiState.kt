package com.illiarb.tmdbclient.feature.search

import com.illiarb.tmdbexplorer.coreui.Cloneable
import com.illiarb.tmdblcient.core.entity.Movie
import java.util.Collections

/**
 * @author ilya-rb on 27.12.18.
 */
data class SearchUiState(
    val isSearchRunning: Boolean,
    val searchResults: List<Movie>,
    val error: Throwable?
) : Cloneable<SearchUiState> {

    companion object {
        fun idle() = SearchUiState(false, Collections.emptyList(), null)
    }

    override fun clone(): SearchUiState = copy()
}