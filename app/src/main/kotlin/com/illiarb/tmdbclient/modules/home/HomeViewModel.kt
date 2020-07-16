package com.illiarb.tmdbclient.modules.home

import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbclient.libs.ui.base.viewmodel.BaseViewModel
import com.illiarb.tmdbclient.libs.ui.common.ErrorMessage
import com.illiarb.tmdbclient.libs.ui.common.ViewStateEvent
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.modules.home.HomeViewModel.Event
import com.illiarb.tmdbclient.modules.home.HomeViewModel.State
import com.illiarb.tmdbclient.navigation.NavigationAction.Home
import com.illiarb.tmdbclient.navigation.NavigationAction.WebViewAction
import com.illiarb.tmdbclient.navigation.Router
import com.illiarb.tmdbclient.services.analytics.AnalyticsService
import com.illiarb.tmdbclient.services.tmdb.domain.Filter
import com.illiarb.tmdbclient.services.tmdb.domain.Genre
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.domain.MovieSection
import com.illiarb.tmdbclient.services.tmdb.domain.TrendingSection
import com.illiarb.tmdbclient.services.tmdb.interactor.FiltersInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.HomeInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.TrendingInteractor
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
  private val homeInteractor: HomeInteractor,
  private val trendingInteractor: TrendingInteractor,
  private val filtersInteractor: FiltersInteractor,
  private val router: Router,
  private val analyticsService: AnalyticsService
) : BaseViewModel<State, Event>(initialState()) {

  companion object {
    private fun initialState() = State(
      isLoadingSections = true,
      sections = emptyList(),
      error = null
    )
  }

  init {
    viewModelScope.launch {
      when (val sections = homeInteractor.getHomeSections()) {
        is Result.Ok -> {
          setState {
            copy(
              isLoadingSections = false,
              sections = sections.data
            )
          }
        }
        is Result.Err -> {
          setState {
            copy(
              isLoadingSections = false,
              error = ViewStateEvent(ErrorMessage(sections.error.message ?: ""))
            )
          }
        }
      }

      trendingInteractor.getTrending().doIfOk {
        val sectionsList = currentState.sections.toMutableList()

        if (sectionsList.isEmpty()) {
          sectionsList.add(TrendingSection(it))
        } else {
          sectionsList.add(1, TrendingSection(it))
        }

        setState {
          copy(sections = sectionsList)
        }
      }
    }
  }

  override fun onUiEvent(event: Event) {
    when (event) {
      is Event.SeeAllClick -> router.executeAction(Home.GoToDiscover)
      is Event.MovieClick -> router.executeAction(Home.GoToMovieDetails(event.movie.id))
      is Event.TmdbIconClick -> router.executeAction(WebViewAction.GoToTmdbPage)
      is Event.DebugClick -> router.executeAction(Home.GoToUiComponents)
      is Event.DiscoverClick -> router.executeAction(Home.GoToDiscover)
      is Event.GenreClick -> onGenreClicked(event.genre)
    }
  }

  private fun onGenreClicked(genre: Genre) = viewModelScope.launch {
    when (val filter = filtersInteractor.getFilter()) {
      is Result.Ok -> {
        when (val updateResult =
          filtersInteractor.saveFilter(Filter.create(genreIds = listOf(genre.id)))) {
          is Result.Ok -> router.executeAction(Home.GoToDiscover)
          is Result.Err -> {
            setState {
              copy(error = ViewStateEvent(ErrorMessage(updateResult.error.message ?: "")))
            }
          }
        }
      }
      is Result.Err -> {
        setState {
          copy(error = ViewStateEvent(ErrorMessage(filter.error.message ?: "")))
        }
      }
    }
  }

  data class State(
    val isLoadingSections: Boolean,
    val sections: List<MovieSection>,
    val error: ViewStateEvent<ErrorMessage>?
  )

  sealed class Event {
    data class MovieClick(val movie: Movie) : Event()
    data class SeeAllClick(val code: String) : Event()
    data class GenreClick(val genre: Genre) : Event()
    object DiscoverClick : Event()
    object TmdbIconClick : Event()
    object DebugClick : Event()
  }
}