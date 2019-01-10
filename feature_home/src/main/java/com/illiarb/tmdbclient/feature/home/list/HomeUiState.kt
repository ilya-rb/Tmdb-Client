package com.illiarb.tmdbclient.feature.home.list

import com.illiarb.tmdbexplorer.coreui.Cloneable
import com.illiarb.tmdblcient.core.entity.MovieSection

/**
 * @author ilya-rb on 09.01.19.
 */
data class HomeUiState(
    val isLoading: Boolean,
    val movies: List<MovieSection>
) : Cloneable<HomeUiState> {

    override fun clone(): HomeUiState = copy()
}