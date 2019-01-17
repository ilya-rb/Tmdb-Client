package com.illiarb.tmdbclient.feature.home.list.presentation

import com.illiarb.tmdbclient.feature.home.list.domain.GetAllMovies
import com.illiarb.tmdbclient.feature.home.list.domain.MovieBlock
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.auth.Authenticator
import com.illiarb.tmdblcient.core.entity.*
import com.illiarb.tmdblcient.core.navigation.*
import kotlinx.coroutines.launch
import java.util.*
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
        launch(context = coroutineContext) {
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