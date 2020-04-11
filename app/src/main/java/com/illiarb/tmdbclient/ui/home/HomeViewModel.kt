package com.illiarb.tmdbclient.ui.home

import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbclient.ui.home.HomeViewModel.Event
import com.illiarb.tmdbclient.ui.home.HomeViewModel.State
import com.illiarb.tmdbexplorer.coreui.base.viewmodel.BaseViewModel
import com.tmdbclient.servicetmdb.domain.Genre
import com.tmdbclient.servicetmdb.domain.Movie
import com.tmdbclient.servicetmdb.domain.MovieSection
import com.tmdbclient.servicetmdb.domain.TrendingSection
import com.illiarb.tmdbclient.navigation.Router
import com.illiarb.tmdbclient.navigation.Router.Action.ShowDiscover
import com.illiarb.tmdbclient.navigation.Router.Action.ShowMovieDetails
import com.illiarb.tmdbclient.navigation.Router.Action.ShowSettings
import com.illiarb.tmdbclient.util.Async
import com.illiarb.tmdclient.analytics.AnalyticsService
import com.tmdbclient.servicetmdb.interactor.HomeInteractor
import com.tmdbclient.servicetmdb.interactor.TrendingInteractor
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
        .doOnError { showMessage(it.message) }

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
      is Event.SeeAllClick -> router.executeAction(ShowDiscover())
        //.also(analyticsService::trackRouterAction)

      is Event.SettingsClick -> router.executeAction(ShowSettings)
        //.also(analyticsService::trackRouterAction)

      is Event.MovieClick ->
        router.executeAction(ShowMovieDetails(event.movie.id))
          //.also(analyticsService::trackRouterAction)

      is Event.GenreClick ->
        router.executeAction(ShowDiscover(event.genre.id))
          //.also(analyticsService::trackRouterAction)
    }
  }

  data class State(val sections: Async<List<MovieSection>>, val error: String?)

  sealed class Event {
    data class MovieClick(val movie: Movie) : Event()
    data class SeeAllClick(val code: String) : Event()
    data class GenreClick(val genre: Genre) : Event()
    object SettingsClick : Event()
  }
}