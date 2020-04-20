package com.illiarb.tmdbclient.ui.discover

import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.libs.ui.base.viewmodel.BaseViewModel
import com.illiarb.tmdbclient.libs.ui.common.Text
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.navigation.Router
import com.illiarb.tmdbclient.navigation.Router.Action.ShowMovieDetails
import com.illiarb.tmdbclient.services.analytics.AnalyticsService
import com.illiarb.tmdbclient.services.tmdb.domain.Genre
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.interactor.GenresInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.MoviesInteractor
import kotlinx.coroutines.launch
import javax.inject.Inject

class DiscoverViewModel @Inject constructor(
  initialGenreId: Int,
  private val router: Router,
  private val moviesInteractor: MoviesInteractor,
  private val genresInteractor: GenresInteractor,
  private val analyticsService: AnalyticsService
) : BaseViewModel<DiscoverViewModel.State, DiscoverViewModel.Event>(initialState()) {

  companion object {

    private fun initialState(): State =
      State(
        results = emptyList(),
        genres = emptyList(),
        screenTitle = Text.AsResource(R.string.discover_genres_all),
        selectedGenreIds = emptyList(),
        isLoading = false,
        isLoadingAdditionalPage = false,
        currentPage = 1,
        totalPages = 0
      )
  }

  override fun onUiEvent(event: Event) {
    when (event) {
      is Event.MovieClick -> processMovieClick(event.movie)
      is Event.ApplyFilter -> applyFilter(event.ids)
      is Event.ClearFilter -> applyFilter()
      is Event.PageEndReached -> fetchLoadNextPageIfExists()
    }
  }

  init {
    viewModelScope.launch {
      when (val genres = genresInteractor.getAllGenres()) {
        is Result.Ok -> setState { copy(genres = genres.data) }
          .also { applyFilter(listOf(initialGenreId), isInitialLaunch = true) }
        is Result.Err -> showMessage(genres.error.message)
      }
    }
  }

  private fun processMovieClick(movie: Movie) {
    router.executeAction(ShowMovieDetails(movie.id))//.also(analyticsService::trackRouterAction)
  }

  private fun applyFilter(genreIds: List<Int> = emptyList(), isInitialLaunch: Boolean = false) {
    viewModelScope.launch {
      // do nothing if this genre are already applied
      if (currentState.selectedGenreIds == genreIds && !isInitialLaunch) {
        return@launch
      }

      setState {
        copy(isLoading = true)
      }

      val results = moviesInteractor.discoverMovies(genreIds, 1)

      setState {
        copy(isLoading = false)
      }

      when (results) {
        is Result.Ok -> {
          setState {
            copy(
              results = results.data.items,
              currentPage = results.data.page,
              totalPages = results.data.totalPages,
              selectedGenreIds = genreIds,
              screenTitle = if (genreIds.isEmpty()) {
                Text.AsResource(R.string.discover_genres_all)
              } else {
                val selected = currentState.genres.filter { genreIds.contains(it.id) }
                if (selected.isEmpty()) {
                  Text.AsResource(R.string.discover_genres_all)
                } else {
                  Text.AsString(selected.joinToString(",") { it.name })
                }
              }
            )
          }
        }
        is Result.Err -> showMessage(results.error.message)
      }
    }
  }

  private fun fetchLoadNextPageIfExists() {
    if (currentState.isLoadingAdditionalPage || currentState.currentPage == currentState.totalPages) {
      return
    }

    viewModelScope.launch {
      setState {
        copy(isLoadingAdditionalPage = true)
      }

      val results =
        moviesInteractor.discoverMovies(currentState.selectedGenreIds, currentState.currentPage + 1)

      when (results) {
        is Result.Ok -> {
          setState {
            copy(
              results = currentState.results.plus(results.data.items),
              isLoadingAdditionalPage = false,
              currentPage = results.data.page,
              totalPages = results.data.totalPages
            )
          }
        }
        is Result.Err -> {
          showMessage(results.error.message)
          setState {
            copy(isLoadingAdditionalPage = false)
          }
        }
      }
    }
  }

  data class State(
    val results: List<Movie>,
    val genres: List<Genre>,
    val screenTitle: Text,
    val selectedGenreIds: List<Int>,
    val isLoading: Boolean,
    val currentPage: Int,
    val totalPages: Int,
    val isLoadingAdditionalPage: Boolean
  )

  sealed class Event {
    class MovieClick(val movie: Movie) : Event()
    class ApplyFilter(val ids: List<Int>) : Event()
    object PageEndReached : Event()
    object ClearFilter : Event()
  }
}