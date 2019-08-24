package com.illiarb.tmdbclient.movies.home

import com.illiarb.tmdbclient.movies.home.HomeView.MovieSectionsState
import com.illiarb.tmdbclient.movies.home.HomeView.SearchResultState
import com.illiarb.tmdbclient.movies.home.HomeView.SingleUiEvent.*
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.domain.MoviesInteractor
import com.illiarb.tmdblcient.core.domain.SearchInteractor
import com.illiarb.tmdblcient.core.domain.entity.*
import com.illiarb.tmdblcient.core.ext.collectWithScope
import com.illiarb.tmdblcient.core.navigation.AccountScreen
import com.illiarb.tmdblcient.core.navigation.AuthScreen
import com.illiarb.tmdblcient.core.navigation.MovieDetailsScreen
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.storage.Authenticator
import com.illiarb.tmdblcient.core.system.featureconfig.FeatureConfig
import com.illiarb.tmdblcient.core.system.featureconfig.FeatureName
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * @author ilya-rb on 08.01.19.
 */
class HomeModel @Inject constructor(
    private val featureConfig: FeatureConfig,
    private val moviesInteractor: MoviesInteractor,
    private val searchInteractor: SearchInteractor,
    private val authenticator: Authenticator,
    private val router: Router
) : BasePresentationModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 400L
    }

    private val searchResultsState = Channel<SearchResultState>(Channel.CONFLATED)

    fun bind(view: HomeView) {
        searchResultsState.consumeAsFlow()
            .collectWithScope(view) { view.searchResultsState(it) }

        flow<MovieSectionsState> {
            val movies = moviesInteractor.getAllMovies()
            emit(MovieSectionsState.Content(movies.asSections()))
        }
            .onStart { emit(MovieSectionsState.Loading) }
            .catch { emit(MovieSectionsState.Failure) }
            .collectWithScope(view) { view.movieSections(it) }

        view.searchQuery
            .debounce(SEARCH_DEBOUNCE_DELAY)
            .filter { it.isNotEmpty() }
            .map {
                val searchResults = searchInteractor.searchMovies(it.toString())
                if (searchResults.isEmpty()) {
                    SearchResultState.Empty
                } else {
                    SearchResultState.Results(searchResults.map { m -> SearchResult(m) })
                }
            }
            .collectWithScope(view) { searchResultsState.send(it) }

        view.searchFocusChange
            .collectWithScope(view) { hasFocus ->
                if (hasFocus) {
                    view.accountVisible(false)
                    view.searchResultsState(SearchResultState.Empty)
                } else {
                    view.accountVisible(featureConfig.isFeatureEnabled(FeatureName.AUTH))
                    view.searchResultsState(SearchResultState.Hidden)
                }
            }

        view.uiEvents.collectWithScope(view) { event ->
            when (event) {
                is ClearSearch -> searchResultsState.send(SearchResultState.Hidden)
                is MovieClick -> router.navigateTo(MovieDetailsScreen(event.movie.id))
                is AccountClick -> {
                    if (authenticator.isAuthenticated()) {
                        router.navigateTo(AccountScreen)
                    } else {
                        router.navigateTo(AuthScreen)
                    }
                }
            }
        }

        view.accountVisible(featureConfig.isFeatureEnabled(FeatureName.AUTH))
    }

    private fun List<MovieBlock>.asSections(): List<MovieSection> =
        map { (filter, movies) ->
            if (filter.code == MovieFilter.TYPE_NOW_PLAYING) {
                NowPlayingSection(filter.name, movies)
            } else {
                ListSection(filter.name, movies)
            }
        }
}