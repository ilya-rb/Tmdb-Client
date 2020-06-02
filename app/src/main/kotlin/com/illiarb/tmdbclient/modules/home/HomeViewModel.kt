package com.illiarb.tmdbclient.modules.home

import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbclient.libs.ui.base.viewmodel.BaseViewModel
import com.illiarb.tmdbclient.libs.ui.common.ErrorMessage
import com.illiarb.tmdbclient.libs.ui.common.ViewStateEvent
import com.illiarb.tmdbclient.libs.util.Async
import com.illiarb.tmdbclient.modules.home.HomeViewModel.Event
import com.illiarb.tmdbclient.modules.home.HomeViewModel.State
import com.illiarb.tmdbclient.navigation.NavigationAction.Home
import com.illiarb.tmdbclient.navigation.NavigationAction.WebViewAction
import com.illiarb.tmdbclient.navigation.Router
import com.illiarb.tmdbclient.services.analytics.AnalyticsService
import com.illiarb.tmdbclient.services.tmdb.domain.Genre
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.domain.MovieSection
import com.illiarb.tmdbclient.services.tmdb.domain.TrendingSection
import com.illiarb.tmdbclient.services.tmdb.interactor.HomeInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.TrendingInteractor
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
  private val homeInteractor: HomeInteractor,
  private val trendingInteractor: TrendingInteractor,
  private val router: Router,
  private val analyticsService: AnalyticsService
) : BaseViewModel<State, Event>(initialState()) {

  companion object {
    private fun initialState() = State(sections = Async.Uninitialized, error = null)
  }

  init {
    viewModelScope.launch {
      setState {
        copy(sections = Async.Loading())
      }

      val sections = homeInteractor.getHomeSections().asAsync()

      sections.doOnError {
        it.message?.let { message ->
          setState {
            copy(error = ViewStateEvent(ErrorMessage(message)))
          }
        }
      }

      setState {
        copy(sections = sections)
      }

      val trending = trendingInteractor.getTrending()

      @Suppress("NAME_SHADOWING")
      trending.doIfOk { trending ->
        currentState.sections.doOnSuccess { sections ->
          val sectionsList = sections.toMutableList()
          if (sectionsList.isEmpty()) {
            sectionsList.add(TrendingSection(trending))
          } else {
            sectionsList.add(1, TrendingSection(trending))
          }
          setState {
            copy(sections = Async.Success(sectionsList))
          }
        }
      }
    }
  }

  override fun onUiEvent(event: Event) {
    when (event) {
      is Event.SeeAllClick -> router.executeAction(Home.GoToDiscover())
      is Event.MovieClick -> router.executeAction(Home.GoToMovieDetails(event.movie.id))
      is Event.GenreClick -> router.executeAction(Home.GoToDiscover(event.genre.id))
      is Event.TmdbIconClick -> router.executeAction(WebViewAction.GoToTmdbPage)
      is Event.DebugClick -> router.executeAction(Home.GoToUiComponents)
      is Event.DiscoverClick -> router.executeAction(Home.GoToDiscover())
    }
  }

  data class State(
    val sections: Async<List<MovieSection>>,
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