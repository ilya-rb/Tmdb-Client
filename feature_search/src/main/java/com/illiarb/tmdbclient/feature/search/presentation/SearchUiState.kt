package com.illiarb.tmdbclient.feature.search.presentation

import com.illiarb.tmdbexplorer.coreui.Cloneable
import com.illiarb.tmdblcient.core.entity.Movie

/**
 * @author ilya-rb on 27.12.18.
 */
data class SearchUiState(
    val icon: SearchIcon,
    val isSearchRunning: Boolean,
    val result: SearchResult,
    val error: Throwable?
) : Cloneable<SearchUiState> {

    companion object {
        fun idle() = SearchUiState(
            SearchIcon.Search,
            false,
            SearchResult.Initial,
            null
        )
    }

    sealed class SearchIcon {
        object Search : SearchIcon()
        object Cross : SearchIcon()
    }

    sealed class SearchResult {
        object Initial : SearchResult()
        object Empty : SearchResult()
        data class Success(val movies: List<Movie>) : SearchResult()
    }

    override fun clone(): SearchUiState = copy()
}