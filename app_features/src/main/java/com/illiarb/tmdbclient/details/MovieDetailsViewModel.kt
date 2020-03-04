package com.illiarb.tmdbclient.details

import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbclient.details.MovieDetailsViewModel.Event
import com.illiarb.tmdbclient.details.MovieDetailsViewModel.State
import com.illiarb.tmdbexplorer.coreui.base.viewmodel.BaseViewModel
import com.illiarb.tmdblcient.core.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowMovieDetails
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowVideos
import com.illiarb.tmdblcient.core.util.Async
import com.illiarb.tmdblcient.core.util.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(
  private val movieId: Int,
  private val moviesInteractor: MoviesInteractor,
  private val router: Router,
  private val analyticsService: AnalyticsService
) : BaseViewModel<State, Event>(initialState()) {

  companion object {
    private fun initialState() = State(movie = Async.Uninitialized, movieSections = emptyList())
  }

  init {
    viewModelScope.launch {
      setState { copy(movie = Async.Loading()) }

      when (val result = moviesInteractor.getMovieDetails(movieId)) {
        is Result.Success -> {
          val movieInfo = MovieInfo(result.data)
          val moviePhotos = MoviePhotos(result.data)

          setState {
            copy(
              movie = Async.Success(result.data),
              movieSections = listOf(movieInfo, moviePhotos)
            )
          }
        }
        is Result.Error -> TODO()
      }
    }

    viewModelScope.launch {
      when (val similar = moviesInteractor.getSimilarMovies(movieId)) {
        is Result.Success -> {
          if (similar.data.isNotEmpty()) {
            setState {
              copy(movieSections = currentState.movieSections.plus(MovieSimilar(similar.data)))
            }
          }
        }
        is Result.Error -> Unit
      }
    }
  }

  override fun onUiEvent(event: Event) {
    when (event) {
      is Event.MovieClicked -> {
        router.executeAction(ShowMovieDetails(event.movie.id))
          .also(analyticsService::trackRouterAction)
      }
      is Event.PlayClicked -> {
        router.executeAction(ShowVideos(movieId))
          .also(analyticsService::trackRouterAction)
      }
    }
  }

  data class State(val movie: Async<Movie>, val movieSections: List<Any>)

  data class MovieInfo(val movie: Movie)

  data class MoviePhotos(val movie: Movie)

  data class MovieSimilar(val movies: List<Movie>)

  sealed class Event {
    object PlayClicked : Event()
    class MovieClicked(val movie: Movie) : Event()
  }
}