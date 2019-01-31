package com.illiarb.tmdbclient.feature.home.list.presentation

import com.illiarb.tmdblcient.core.domain.entity.MovieSection
import com.illiarb.tmdblcient.core.util.Cloneable

/**
 * @author ilya-rb on 09.01.19.
 */
data class HomeUiState(
    val isLoading: Boolean,
    val movies: List<MovieSection>,
    val isSearchEnabled: Boolean,
    val isAuthEnabled: Boolean
) : Cloneable<HomeUiState> {

    override fun clone(): HomeUiState = copy()
}