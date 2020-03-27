package com.illiarb.tmdbclient.discover

import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.base.viewmodel.BaseViewModel
import com.illiarb.tmdbexplorer.coreui.common.Text
import com.illiarb.tmdblcient.core.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.interactor.GenresInteractor
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowMovieDetails
import com.illiarb.tmdblcient.core.util.Result
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
        selectedGenreId = Genre.GENRE_ALL,
        isLoading = false
      )
  }

  override fun onUiEvent(event: Event) {
    when (event) {
      is Event.MovieClick -> processMovieClick(event.movie)
      is Event.ApplyFilter -> applyFilter(event.id)
      is Event.ClearFilter -> applyFilter()
    }
  }

  init {
    viewModelScope.launch {
      when (val genres = genresInteractor.getAllGenres()) {
        is Result.Ok -> setState { copy(genres = genres.data) }
          .also { applyFilter(initialGenreId, isInitialLaunch = true) }
        is Result.Err -> showMessage(genres.error.message)
      }
    }
  }

  private fun processMovieClick(movie: Movie) {
    router.executeAction(ShowMovieDetails(movie.id)).also(analyticsService::trackRouterAction)
  }

  private fun applyFilter(genreId: Int = Genre.GENRE_ALL, isInitialLaunch: Boolean = false) {
    viewModelScope.launch {
      // do nothing if this genre are already applied
      if (currentState.selectedGenreId == genreId && !isInitialLaunch) {
        return@launch
      }

      setState {
        copy(isLoading = true)
      }

      val results = moviesInteractor.discoverMovies(genreId)

      setState {
        copy(isLoading = false)
      }

      when (results) {
        is Result.Ok -> {
          setState {
            copy(
              results = results.data,
              selectedGenreId = genreId,
              screenTitle = if (genreId == Genre.GENRE_ALL) {
                Text.AsResource(R.string.discover_genres_all)
              } else {
                val selected = currentState.genres.find { it.id == genreId }
                if (selected == null) {
                  Text.AsResource(R.string.discover_genres_all)
                } else {
                  Text.AsString(selected.name)
                }
              }
            )
          }
        }
        is Result.Err -> showMessage(results.error.message)
      }
    }
  }

  data class State(
    val results: List<Movie>,
    val genres: List<Genre>,
    val screenTitle: Text,
    val selectedGenreId: Int,
    val isLoading: Boolean
  )

  sealed class Event {
    class MovieClick(val movie: Movie) : Event()
    class ApplyFilter(val id: Int) : Event()
    object ClearFilter : Event()
  }
}