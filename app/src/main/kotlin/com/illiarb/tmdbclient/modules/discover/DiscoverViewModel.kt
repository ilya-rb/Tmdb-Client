package com.illiarb.tmdbclient.modules.discover

import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbclient.libs.ui.base.viewmodel.BaseViewModel
import com.illiarb.tmdbclient.libs.ui.common.ErrorMessage
import com.illiarb.tmdbclient.libs.ui.common.ViewStateEvent
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.navigation.NavigationAction
import com.illiarb.tmdbclient.navigation.Router
import com.illiarb.tmdbclient.services.analytics.AnalyticsService
import com.illiarb.tmdbclient.services.tmdb.domain.Filter
import com.illiarb.tmdbclient.services.tmdb.domain.Genre
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.interactor.FiltersInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.GenresInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.MoviesInteractor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class DiscoverViewModel @Inject constructor(
  initialGenreId: Int,
  private val router: Router,
  private val moviesInteractor: MoviesInteractor,
  private val genresInteractor: GenresInteractor,
  private val analyticsService: AnalyticsService,
  private val filtersInteractor: FiltersInteractor
) : BaseViewModel<DiscoverViewModel.State, DiscoverViewModel.Event>(initialState()) {

  companion object {

    private fun initialState(): State =
      State(
        results = emptyList(),
        availableGenres = emptyList(),
        filter = Filter.empty(),
        isLoading = false,
        isLoadingAdditionalPage = false,
        currentPage = 1,
        totalPages = 0,
        errorMessage = null
      )
  }

  override fun onUiEvent(event: Event) {
    when (event) {
      is Event.MovieClick -> processMovieClick(event.movie)
      is Event.PageEndReached -> fetchLoadNextPageIfExists()
      is Event.FilterClicked -> router.executeAction(NavigationAction.Discover.GoToFilters)
    }
  }

  init {
//    viewModelScope.launch {
//      val genres = genresInteractor.getAllGenres()
//
//      setState {
//        when (genres) {
//          is Result.Ok -> {
//            copy(availableGenres = genres.data).also {
//              applyFilter(
//                filter = Filter(listOf(initialGenreId), YearConstraints.AllYears),
//                isInitialLaunch = true
//              )
//            }
//          }
//          is Result.Err -> {
//            copy(errorMessage = ViewStateEvent(ErrorMessage(genres.error.message ?: "")))
//          }
//        }
//      }
//    }

    viewModelScope.launch {
      filtersInteractor.filter.collect {
        applyFilter(it, isInitialLaunch = false)
      }
    }
  }

  private fun processMovieClick(movie: Movie) {
    router.executeAction(NavigationAction.Discover.GoToMovieDetails(movie.id))
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

      val results = moviesInteractor.discoverMovies(filter, 1)

      setState {
        when (results) {
          is Result.Ok -> {
            copy(
              isLoading = false,
              results = results.data.items,
              currentPage = results.data.page,
              totalPages = results.data.totalPages,
              filter = filter
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

  private fun fetchLoadNextPageIfExists() {
    if (currentState.isLoadingAdditionalPage ||
      currentState.currentPage == currentState.totalPages
    ) {
      return
    }

    viewModelScope.launch {
      setState {
        copy(isLoadingAdditionalPage = true)
      }

      val results = moviesInteractor.discoverMovies(
        filter = currentState.filter,
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
    val isLoading: Boolean,
    val currentPage: Int,
    val totalPages: Int,
    val isLoadingAdditionalPage: Boolean,
    val errorMessage: ViewStateEvent<ErrorMessage>?
  )

  sealed class Event {
    class MovieClick(val movie: Movie) : Event()
    object PageEndReached : Event()
    object FilterClicked : Event()
  }
}