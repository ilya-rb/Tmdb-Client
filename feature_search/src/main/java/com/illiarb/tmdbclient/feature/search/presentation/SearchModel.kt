package com.illiarb.tmdbclient.feature.search.presentation

import com.illiarb.tmdbclient.feature.search.presentation.SearchUiState.SearchIcon
import com.illiarb.tmdbclient.feature.search.presentation.SearchUiState.SearchResult
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.domain.SearchInteractor
import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.navigation.MovieDetailsScreen
import com.illiarb.tmdblcient.core.navigation.Router
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
@ExperimentalCoroutinesApi
class SearchModel @Inject constructor(
    private val searchInteractor: SearchInteractor,
    private val router: Router
) : BasePresentationModel<SearchUiState>(SearchUiState.idle()) {

    fun search(query: String) = runCoroutine {
        setState {
            copy(icon = SearchIcon.Cross, isSearchRunning = true)
        }

        handleResult(searchInteractor.searchMovies(query), { movies ->
            val searchResult =
                if (movies.isEmpty()) SearchResult.Empty else SearchResult.Success(movies)
            setState {
                copy(isSearchRunning = false, result = searchResult)
            }
        })
    }

    fun onClearClicked() {
        setState {
            copy(icon = SearchIcon.Search)
        }
    }

    fun onMovieClicked(movie: Movie) {
        router.navigateTo(MovieDetailsScreen(movie.id))
    }
}