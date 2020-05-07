package com.illiarb.tmdbclient.ui.details

import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbclient.libs.ui.base.viewmodel.BaseViewModel
import com.illiarb.tmdbclient.libs.ui.common.ErrorMessage
import com.illiarb.tmdbclient.libs.ui.common.ViewStateEvent
import com.illiarb.tmdbclient.libs.util.Async
import com.illiarb.tmdbclient.navigation.Action.ShowMovieDetails
import com.illiarb.tmdbclient.navigation.Action.ShowVideos
import com.illiarb.tmdbclient.navigation.Router
import com.illiarb.tmdbclient.services.analytics.AnalyticsService
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.services.tmdb.interactor.MoviesInteractor
import com.illiarb.tmdbclient.ui.details.MovieDetailsViewModel.Event
import com.illiarb.tmdbclient.ui.details.MovieDetailsViewModel.State
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(
  private val movieId: Int,
  private val moviesInteractor: MoviesInteractor,
  private val router: Router,
  private val analyticsService: AnalyticsService
) : BaseViewModel<State, Event>(initialState()) {

  companion object {
    private fun initialState() =
      State(
        movie = Async.Uninitialized,
        movieSections = emptyList(),
        error = null
      )
  }

  init {
    viewModelScope.launch {
      setState {
        copy(movie = Async.Loading())
      }

      val movieDetails = moviesInteractor.getMovieDetails(movieId).asAsync()

      movieDetails.doOnSuccess { movie ->
        val sections = mutableListOf<Any>().apply {
          add(MovieInfo(movie))

          if (movie.images.isNotEmpty()) {
            add(MoviePhotos(movie))
          }

          val similar = moviesInteractor.getSimilarMovies(movieId)
          similar.doIfOk {
            add(MovieSimilar(it))
          }
        }

        setState {
          copy(
            movie = movieDetails,
            movieSections = sections
          )
        }
      }

      movieDetails.doOnError { error ->
        setState {
          copy(
            movie = movieDetails,
            error = ViewStateEvent(ErrorMessage(error.message ?: ""))
          )
        }
      }
    }
  }

  override fun onUiEvent(event: Event) {
    when (event) {
      is Event.MovieClicked -> {
        router.executeAction(ShowMovieDetails(event.movie.id))
        //.also(analyticsService::trackRouterAction)
      }
      is Event.PlayClicked -> {
        router.executeAction(ShowVideos(movieId))
        //.also(analyticsService::trackRouterAction)
      }
    }
  }

  data class State(
    val movie: Async<Movie>,
    val movieSections: List<Any>,
    val error: ViewStateEvent<ErrorMessage>?
  )

  data class MovieInfo(val movie: Movie)

  data class MoviePhotos(val movie: Movie)

  data class MovieSimilar(val movies: List<Movie>)

  sealed class Event {
    object PlayClicked : Event()
    class MovieClicked(val movie: Movie) : Event()
  }
}