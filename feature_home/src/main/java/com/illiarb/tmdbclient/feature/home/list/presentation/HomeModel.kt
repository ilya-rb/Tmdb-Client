package com.illiarb.tmdbclient.feature.home.list.presentation

import com.illiarb.tmdbclient.feature.home.list.domain.GetAllMovies
import com.illiarb.tmdbclient.feature.home.list.domain.MovieBlock
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.auth.Authenticator
import com.illiarb.tmdblcient.core.config.FeatureConfig
import com.illiarb.tmdblcient.core.config.FeatureName
import com.illiarb.tmdblcient.core.entity.*
import com.illiarb.tmdblcient.core.navigation.*
import java.util.*
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class HomeModel @Inject constructor(
    featureConfig: FeatureConfig,
    private val getAllMovies: GetAllMovies,
    private val authenticator: Authenticator,
    private val router: Router
) : BasePresentationModel<HomeUiState>() {

    init {
        val initialState = HomeUiState(
            isLoading = true,
            movies = Collections.emptyList(),
            isSearchEnabled = featureConfig.isFeatureEnabled(FeatureName.SEARCH),
            isAuthEnabled = featureConfig.isFeatureEnabled(FeatureName.AUTH)
        )

        setIdleState(initialState)

        runCoroutine {
            handleResult(getAllMovies.executeAsync(Unit), { blocks ->
                val sections = createMovieSections(blocks)
                setState { current ->
                    current.copy(isLoading = false, movies = sections)
                }
            })
        }
    }

    fun onSearchClick() {
        router.navigateTo(SearchScreen)
    }

    fun onMovieClick(movie: Movie) {
        router.navigateTo(MovieDetailsScreen(movie.id))
    }

    fun onAccountClick() {
        runCoroutine {
            if (authenticator.isAuthenticated()) {
                router.navigateTo(AccountScreen)
            } else {
                router.navigateTo(AuthScreen)
            }
        }
    }

    private fun createMovieSections(movies: List<MovieBlock>): List<MovieSection> =
        movies.map { (filter, movies) ->
            if (filter.code == MovieFilter.TYPE_NOW_PLAYING) {
                NowPlayingSection(filter.name, movies)
            } else {
                ListSection(filter.name, movies)
            }
        }
}