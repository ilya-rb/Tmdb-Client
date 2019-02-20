package com.illiarb.tmdbclient.feature.home.list.presentation

import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.domain.MoviesInteractor
import com.illiarb.tmdblcient.core.domain.entity.ListSection
import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.domain.entity.MovieBlock
import com.illiarb.tmdblcient.core.domain.entity.MovieSection
import com.illiarb.tmdblcient.core.navigation.*
import com.illiarb.tmdblcient.core.storage.Authenticator
import com.illiarb.tmdblcient.core.system.featureconfig.FeatureConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
@ExperimentalCoroutinesApi
class HomeModel @Inject constructor(
    featureConfig: FeatureConfig,
    private val moviesInteractor: MoviesInteractor,
    private val authenticator: Authenticator,
    private val router: Router
) : BasePresentationModel<HomeUiState>(HomeUiState.idle(featureConfig)) {

    init {
        runCoroutine {
            handleResult(moviesInteractor.getAllMovies(), { blocks ->
                setState { copy(isLoading = false, movies = createMovieSections(blocks)) }
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
            ListSection(filter.name, movies)
        }
}