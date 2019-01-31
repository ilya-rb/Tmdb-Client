package com.illiarb.tmdbclient.feature.home.list.presentation

import com.illiarb.tmdblcient.core.domain.entity.MovieSection
import com.illiarb.tmdblcient.core.system.featureconfig.FeatureConfig
import com.illiarb.tmdblcient.core.system.featureconfig.FeatureName
import com.illiarb.tmdblcient.core.util.Cloneable
import java.util.*

/**
 * @author ilya-rb on 09.01.19.
 */
data class HomeUiState(
    val isLoading: Boolean,
    val movies: List<MovieSection>,
    val isSearchEnabled: Boolean,
    val isAuthEnabled: Boolean
) : Cloneable<HomeUiState> {

    companion object {

        fun idle(featureConfig: FeatureConfig) =
            HomeUiState(
                false,
                Collections.emptyList(),
                featureConfig.isFeatureEnabled(FeatureName.SEARCH),
                featureConfig.isFeatureEnabled(FeatureName.AUTH)
            )
    }

    override fun clone(): HomeUiState = copy()
}