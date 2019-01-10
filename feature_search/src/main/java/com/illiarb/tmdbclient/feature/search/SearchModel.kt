package com.illiarb.tmdbclient.feature.search

import com.illiarb.tmdbclient.feature.search.SearchUiState.SearchIcon
import com.illiarb.tmdbclient.feature.search.SearchUiState.SearchResult
import com.illiarb.tmdbclient.feature.search.domain.SearchMovies
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.navigation.MovieDetailsScreen
import com.illiarb.tmdblcient.core.navigation.Router
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class SearchModel @Inject constructor(
    private val searchMovies: SearchMovies,
    private val router: Router
) : BasePresentationModel<SearchUiState>() {

    init {
        setIdleState(SearchUiState.idle())
    }

    fun search(query: String) = launch(context = coroutineContext) {
        setState {
            SearchUiState(SearchIcon.Cross, true, it.result, it.error)
        }

        try {
            val movies = searchMovies.execute(query)
            val result = if (movies.isEmpty()) {
                SearchResult.Empty
            } else {
                SearchResult.Success(movies)
            }

            setState {
                SearchUiState(it.icon, false, result, it.error)
            }
        } catch (e: Exception) {
            // Process error
        }
    }

    fun onClearClicked() {
        setState {
            SearchUiState(SearchIcon.Search, it.isSearchRunning, it.result, it.error)
        }
    }

    fun onMovieClicked(movie: Movie) {
        router.navigateTo(MovieDetailsScreen(movie.id))
    }
}