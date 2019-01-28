package com.illiarb.tmdbclient.feature.search.presentation

import com.illiarb.tmdbclient.feature.search.domain.SearchMovies
import com.illiarb.tmdbclient.feature.search.presentation.SearchUiState.SearchIcon
import com.illiarb.tmdbclient.feature.search.presentation.SearchUiState.SearchResult
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.navigation.MovieDetailsScreen
import com.illiarb.tmdblcient.core.navigation.Router
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
@ExperimentalCoroutinesApi
class SearchModel @Inject constructor(
    private val searchMovies: SearchMovies,
    private val router: Router
) : BasePresentationModel<SearchUiState>() {

    init {
        setIdleState(SearchUiState.idle())
    }

    fun search(query: String) = runCoroutine {
        setState {
            it.copy(icon = SearchIcon.Cross, isSearchRunning = true)
        }

        handleResult(searchMovies.executeAsync(query), { movies ->
            val searchResult = if (movies.isEmpty()) SearchResult.Empty else SearchResult.Success(movies)
            setState {
                it.copy(isSearchRunning = false, result = searchResult)
            }
        })
    }

    fun onClearClicked() {
        setState {
            it.copy(icon = SearchIcon.Search)
        }
    }

    fun onMovieClicked(movie: Movie) {
        router.navigateTo(MovieDetailsScreen(movie.id))
    }
}