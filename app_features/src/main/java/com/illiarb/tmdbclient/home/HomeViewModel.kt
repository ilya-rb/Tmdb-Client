package com.illiarb.tmdbclient.home

import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbclient.home.HomeViewModel.Event
import com.illiarb.tmdbclient.home.HomeViewModel.State
import com.illiarb.tmdbexplorer.coreui.base.viewmodel.BaseViewModel
import com.illiarb.tmdblcient.core.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.domain.TrendingSection
import com.illiarb.tmdblcient.core.interactor.HomeInteractor
import com.illiarb.tmdblcient.core.interactor.TrendingInteractor
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowDiscover
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowMovieDetails
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowSettings
import com.illiarb.tmdblcient.core.util.Async
import com.illiarb.tmdblcient.core.util.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
  private val homeInteractor: HomeInteractor,
  private val trendingInteractor: TrendingInteractor,
  private val router: Router,
  private val analyticsService: AnalyticsService
) : BaseViewModel<State, Event>(initialState()) {

  companion object {
    private fun initialState() = State(sections = Async.Uninitialized)
  }

  init {
    viewModelScope.launch {
      setState { copy(sections = Async.Loading()) }

      val sections = homeInteractor.getHomeSections().asAsync()
        .doOnError { showMessage(it.message) }

      setState {
        copy(sections = sections)
      }

      val trending = trendingInteractor.getTrending()
      if (trending is Result.Success) {
        currentState.sections.doOnSuccess {
          val sectionsList = it.toMutableList()
          if (sectionsList.isEmpty()) {
            sectionsList.add(TrendingSection(trending.data))
          } else {
            sectionsList.add(1, TrendingSection(trending.data))
          }
          setState {
            copy(sections = Async.Success(sectionsList))
          }
        }
      } else {
        showMessage(trending.error().message)
      }
    }
  }

  override fun onUiEvent(event: Event) {
    when (event) {
      is Event.SeeAllClick -> router.executeAction(ShowDiscover())
        .also(analyticsService::trackRouterAction)

      is Event.SettingsClick -> router.executeAction(ShowSettings)
        .also(analyticsService::trackRouterAction)

      is Event.MovieClick ->
        router.executeAction(ShowMovieDetails(event.movie.id))
          .also(analyticsService::trackRouterAction)

      is Event.GenreClick ->
        router.executeAction(ShowDiscover(event.genre.id))
          .also(analyticsService::trackRouterAction)
    }
  }

  data class State(val sections: Async<List<MovieSection>>)

  sealed class Event {
    data class MovieClick(val movie: Movie) : Event()
    data class SeeAllClick(val code: String) : Event()
    data class GenreClick(val genre: Genre) : Event()
    object SettingsClick : Event()
  }
}