package com.illiarb.tmdbclient.modules.home

import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.libs.ui.base.viewmodel.BaseViewModel
import com.illiarb.tmdbclient.libs.ui.common.ErrorMessage
import com.illiarb.tmdbclient.libs.ui.common.ViewStateEvent
import com.illiarb.tmdbclient.libs.util.Async
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
import com.illiarb.tmdbclient.system.DayNightModePreferences
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
  private val homeInteractor: HomeInteractor,
  private val trendingInteractor: TrendingInteractor,
  private val filtersInteractor: FiltersInteractor,
  private val router: Router,
  private val analyticsService: AnalyticsService,
  private val dayNightModePreferences: DayNightModePreferences
) : BaseViewModel<State, Event>(initialState(dayNightModePreferences.isNightModeEnabled)) {

  companion object {

    private fun initialState(isNightModeEnabled: Boolean) = State(
      sections = Async.Loading(),
      error = null,
      dayNightModeIconRes = getDayNightModeDrawableRes(isNightModeEnabled)
    )

    private fun getDayNightModeDrawableRes(isNightModeEnabled: Boolean): Int {
      return if (isNightModeEnabled) {
        R.drawable.ic_night_mode_on
      } else {
        R.drawable.ic_night_mode_off
      }
    }
  }

  init {
    viewModelScope.launch {
      val sections = homeInteractor.getHomeSections().asAsync()

      setState {
        copy(
          sections = sections,
          error = sections.errorOrNull()?.let {
            ViewStateEvent(ErrorMessage(it.message ?: ""))
          }
        )
      }

      trendingInteractor.getTrending().doIfOk { response ->
        currentState.sections.doOnSuccess {
          val sectionsList = it.toMutableList()

          if (sectionsList.isEmpty()) {
            sectionsList.add(TrendingSection(response))
          } else {
            sectionsList.add(1, TrendingSection(response))
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
      is Event.SeeAllClick -> router.executeAction(Home.GoToDiscover)
      is Event.MovieClick -> router.executeAction(Home.GoToMovieDetails(event.movie.id))
      is Event.TmdbIconClick -> router.executeAction(WebViewAction.GoToTmdbPage)
      is Event.DebugClick -> router.executeAction(Home.GoToUiComponents)
      is Event.DiscoverClick -> router.executeAction(Home.GoToDiscover)
      is Event.GenreClick -> onGenreClicked(event.genre)
      is Event.DayNightClick -> toggleDayNightMode()
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

  private fun toggleDayNightMode() {
    dayNightModePreferences.toggleDayNightMode()

    setState {
      copy(
        dayNightModeIconRes = getDayNightModeDrawableRes(dayNightModePreferences.isNightModeEnabled)
      )
    }
  }

  data class State(
    val sections: Async<List<MovieSection>>,
    val error: ViewStateEvent<ErrorMessage>?,
    val dayNightModeIconRes: Int
  )

  sealed class Event {
    data class MovieClick(val movie: Movie) : Event()
    data class SeeAllClick(val code: String) : Event()
    data class GenreClick(val genre: Genre) : Event()
    object DiscoverClick : Event()
    object DayNightClick : Event()
    object TmdbIconClick : Event()
    object DebugClick : Event()
  }
}