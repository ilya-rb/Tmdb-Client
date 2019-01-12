package com.illiarb.tmdbclient.feature.home.list.presentation

import com.illiarb.tmdbclient.feature.home.list.domain.GetAllMovies
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.auth.Authenticator
import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.entity.ListSection
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.entity.MovieSection
import com.illiarb.tmdblcient.core.entity.NowPlayingSection
import com.illiarb.tmdblcient.core.navigation.AccountScreen
import com.illiarb.tmdblcient.core.navigation.AuthScreen
import com.illiarb.tmdblcient.core.navigation.MovieDetailsScreen
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.navigation.SearchScreen
import kotlinx.coroutines.launch
import java.util.Collections
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class HomeModel @Inject constructor(
    private val getAllMovies: GetAllMovies,
    private val authenticator: Authenticator,
    private val router: Router
) : BasePresentationModel<HomeUiState>() {

    init {
        setIdleState(HomeUiState(true, Collections.emptyList()))

        launch(context = coroutineContext) {
            val result = getAllMovies.executeAsync(Unit)

            when (result) {
                is Result.Success -> {
                    val movies = createMovieSections(result.result)
                    setState { it.copy(isLoading = false, movies = movies) }
                }
                is Result.Error -> {
                    // Process error
                }
            }
        }
    }

    fun onSearchClick() {
        router.navigateTo(SearchScreen)
    }

    fun onMovieClick(movie: Movie) {
        router.navigateTo(MovieDetailsScreen(movie.id))
    }

    fun onAccountClick() {
        launch(context = coroutineContext) {
            if (authenticator.isAuthenticated()) {
                router.navigateTo(AccountScreen)
            } else {
                router.navigateTo(AuthScreen)
            }
        }
    }

    private fun createMovieSections(movies: List<Pair<MovieFilter, List<Movie>>>): List<MovieSection> =
        movies.map { (filter, movies) ->
            if (filter.code == MovieFilter.TYPE_NOW_PLAYING) {
                NowPlayingSection(filter.name, movies)
            } else {
                ListSection(filter.name, movies)
            }
        }
}