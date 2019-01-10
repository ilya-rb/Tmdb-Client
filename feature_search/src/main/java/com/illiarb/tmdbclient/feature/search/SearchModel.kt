package com.illiarb.tmdbclient.feature.search

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
        setState { SearchUiState.idle() }
    }

    fun search(query: String) = launch(context = coroutineContext) {
        setState {
            SearchUiState(true, it.searchResults, it.error)
        }

        try {
            val results = searchMovies.execute(query)
            setState {
                SearchUiState(false, results, it.error)
            }
        } catch (e: Exception) {
            // Process error
        }
    }

    fun onMovieClicked(movie: Movie) {
        router.navigateTo(MovieDetailsScreen(movie.id))
    }
}