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
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.interactor.DiscoverInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.FiltersInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.SearchInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class DiscoverViewModel @Inject constructor(
  private val router: Router,
  private val analyticsService: AnalyticsService,
  private val filtersInteractor: FiltersInteractor,
  private val searchInteractor: SearchInteractor,
  private val discoverInteractor: DiscoverInteractor
) : BaseViewModel<DiscoverViewModel.State, DiscoverViewModel.Event>(initialState()) {

  companion object {

    private fun initialState(): State =
      State(
        results = emptyList(),
        searchResults = emptyList(),
        userMode = State.UserMode.Default,
        filter = Filter.create(),
        isLoading = false,
        isLoadingAdditionalPage = false,
        currentPage = 1,
        totalPages = 0,
        errorMessage = null
      )
  }

  private var searchJob: Job? = null

  init {
    viewModelScope.launch {
      filtersInteractor.filter.collect {
        applyFilter(it)
      }
    }
  }

  override fun onUiEvent(event: Event) {
    when (event) {
      is Event.MovieClick -> processMovieClick(event.movie)
      is Event.PageEndReached -> fetchLoadNextPageIfExists()
      is Event.FilterClicked -> router.executeAction(NavigationAction.Discover.GoToFilters)
      is Event.QueryTextChanged -> onQueryTextChanged(event.query)
      is Event.SearchClosed -> switchToUserMode(State.UserMode.Default)
      is Event.SearchClicked -> switchToUserMode(State.UserMode.Search)
      is Event.BackClicked -> {
        if (currentState.userMode == State.UserMode.Search) {
          switchToUserMode(State.UserMode.Default)
        } else {
          router.executeAction(NavigationAction.Exit)
        }
      }
    }
  }

  private fun processMovieClick(movie: Movie) {
    router.executeAction(NavigationAction.MovieDetails(movie.id))
  }

  private fun switchToUserMode(mode: State.UserMode) {
    searchJob?.cancel()
    setState {
      copy(isLoading = false, userMode = mode)
    }
  }

  private fun applyFilter(filter: Filter) {
    viewModelScope.launch {
      setState {
        copy(isLoading = true)
      }

      val results = discoverInteractor.discoverMovies(filter, page = 1)

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

      val results = discoverInteractor.discoverMovies(
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

  private fun onQueryTextChanged(query: String) {
    searchJob?.cancel()
    searchJob = viewModelScope.launch {
      setState {
        copy(isLoading = true)
      }

      when (val results = searchInteractor.searchMovies(query)) {
        is Result.Ok -> {
          setState {
            copy(isLoading = false, searchResults = results.data.items)
          }
        }
        is Result.Err -> {
          setState {
            copy(
              isLoading = false,
              errorMessage = ViewStateEvent(ErrorMessage(results.error.message ?: ""))
            )
          }
        }
      }
    }
  }

  data class State(
    val results: List<Movie>,
    val searchResults: List<Movie>,
    val userMode: UserMode,
    val filter: Filter,
    val isLoading: Boolean,
    val currentPage: Int,
    val totalPages: Int,
    val isLoadingAdditionalPage: Boolean,
    val errorMessage: ViewStateEvent<ErrorMessage>?
  ) {

    enum class UserMode {
      Default,
      Search
    }
  }

  sealed class Event {
    class MovieClick(val movie: Movie) : Event()
    class QueryTextChanged(val query: String) : Event()
    object SearchClosed : Event()
    object SearchClicked : Event()
    object BackClicked : Event()
    object PageEndReached : Event()
    object FilterClicked : Event()
  }
}