package com.illiarb.tmdbclient.modules.discover

import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.libs.ui.base.viewmodel.BaseViewModel
import com.illiarb.tmdbclient.libs.ui.common.ErrorMessage
import com.illiarb.tmdbclient.libs.ui.common.Text
import com.illiarb.tmdbclient.libs.ui.common.ViewStateEvent
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.navigation.Action.ShowMovieDetails
import com.illiarb.tmdbclient.navigation.Router
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
        filter = Filter(selectedGenreIds = emptyList(), yearConstraints = YearConstraints.AllYears),
        screenTitle = Text.AsResource(R.string.discover_genres_all),
        availableGenres = emptyList(),
        isLoading = false,
        isLoadingAdditionalPage = false,
        currentPage = 1,
        totalPages = 0,
        errorMessage = null,
        yearsDialog = null
      )
  }

  override fun onUiEvent(event: Event) {
    when (event) {
      is Event.MovieClick -> processMovieClick(event.movie)
      is Event.ApplyFilter -> applyFilter(event.filter)
      is Event.ClearFilter -> applyFilter(Filter.empty())
      is Event.PageEndReached -> fetchLoadNextPageIfExists()
      is Event.YearsFilterClicked -> setState {
        copy(yearsDialog = ViewStateEvent(YearConstraints.generateAvailableConstraints()))
      }
    }
  }

  init {
    viewModelScope.launch {
      val genres = genresInteractor.getAllGenres()

      setState {
        when (genres) {
          is Result.Ok -> {
            copy(availableGenres = genres.data).also {
              applyFilter(
                filter = Filter(listOf(initialGenreId), YearConstraints.AllYears),
                isInitialLaunch = true
              )
            }
          }
          is Result.Err -> {
            copy(errorMessage = ViewStateEvent(ErrorMessage(genres.error.message ?: "")))
          }
        }
      }
    }
  }

  private fun processMovieClick(movie: Movie) {
    router.executeAction(ShowMovieDetails(movie.id))//.also(analyticsService::trackRouterAction)
  }

  private fun applyFilter(filter: Filter, isInitialLaunch: Boolean = false) {
    viewModelScope.launch {
      // do nothing if this filter are already applied
      if (currentState.filter == filter && !isInitialLaunch) {
        return@launch
      }

      setState {
        copy(isLoading = true)
      }

      val results = moviesInteractor.discoverMovies(filter.selectedGenreIds, 1)

      setState {
        when (results) {
          is Result.Ok -> {
            copy(
              isLoading = false,
              results = results.data.items,
              currentPage = results.data.page,
              totalPages = results.data.totalPages,
              filter = filter,
              screenTitle = createGenresTitle(filter.selectedGenreIds)
            )
          }
          is Result.Err -> {
            copy(
              isLoading = false,
              errorMessage = ViewStateEvent(ErrorMessage(results.error.message ?: ""))
            )
          }
        }
      }
    }
  }

  private fun createGenresTitle(genreIds: List<Int>): Text {
    return if (genreIds.isEmpty()) {
      Text.AsResource(R.string.discover_genres_all)
    } else {
      val selected = currentState.availableGenres.filter { genreIds.contains(it.id) }
      if (selected.isEmpty()) {
        Text.AsResource(R.string.discover_genres_all)
      } else {
        Text.AsString(selected.joinToString(",") { it.name })
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

      val results = moviesInteractor.discoverMovies(
        genreIds = currentState.filter.selectedGenreIds,
        page = currentState.currentPage + 1
      )

      setState {
        when (results) {
          is Result.Ok -> {
            copy(
              results = currentState.results.plus(results.data.items),
              isLoadingAdditionalPage = false,
              currentPage = results.data.page,
              totalPages = results.data.totalPages
            )
          }
          is Result.Err -> {
            copy(
              isLoadingAdditionalPage = false,
              errorMessage = ViewStateEvent(ErrorMessage(results.error.message ?: ""))
            )
          }
        }
      }
    }
  }

  data class State(
    val results: List<Movie>,
    val availableGenres: List<Genre>,
    val filter: Filter,
    val screenTitle: Text,
    val isLoading: Boolean,
    val currentPage: Int,
    val totalPages: Int,
    val isLoadingAdditionalPage: Boolean,
    val errorMessage: ViewStateEvent<ErrorMessage>?,
    val yearsDialog: ViewStateEvent<List<YearConstraints>>?
  )

  sealed class Event {
    class MovieClick(val movie: Movie) : Event()
    class ApplyFilter(val filter: Filter) : Event()
    object YearsFilterClicked : Event()
    object PageEndReached : Event()
    object ClearFilter : Event()
  }
}