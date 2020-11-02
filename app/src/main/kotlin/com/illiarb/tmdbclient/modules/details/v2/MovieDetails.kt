package com.illiarb.tmdbclient.modules.details.v2

import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.libs.ui.v2.theme.Size
import com.illiarb.tmdbclient.libs.ui.v2.theme.size
import com.illiarb.tmdbclient.libs.util.Async
import com.illiarb.tmdbclient.modules.details.MovieDetailsViewModel
import com.illiarb.tmdbclient.modules.details.MovieDetailsViewModel.Event
import com.illiarb.tmdbclient.modules.details.MovieDetailsViewModel.MovieDetailsSection.MovieSimilar
import com.illiarb.tmdbclient.modules.details.MovieDetailsViewModel.State
import com.illiarb.tmdbclient.modules.details.v2.components.HorizontalList
import com.illiarb.tmdbclient.modules.details.v2.components.MovieCard
import com.illiarb.tmdbclient.modules.details.v2.components.MovieInfo
import com.illiarb.tmdbclient.modules.details.v2.components.MovieInfoParams
import com.illiarb.tmdbclient.modules.details.v2.components.MovieOverview
import com.illiarb.tmdbclient.modules.details.v2.components.MoviePhoto
import com.illiarb.tmdbclient.modules.details.v2.components.MoviePoster
import com.illiarb.tmdbclient.modules.details.v2.components.getListItemPadding
import com.illiarb.tmdbclient.modules.details.v2.util.navigationBarsPadding
import com.illiarb.tmdbclient.modules.details.v2.util.statusBarsPadding
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

@Composable
fun MovieDetails(
  context: CoroutineContext,
  state: StateFlow<State>,
  eventsSender: SendChannel<Event>,
) {
  val viewState = state.collectAsState(context = context)

  Surface(modifier = Modifier.fillMaxSize()) {
    when (val movieState = viewState.value.movie) {
      is Async.Uninitialized, is Async.Loading -> ProgressState()
      is Async.Fail -> ErrorState()
      is Async.Success -> {
        ScrollableColumn(modifier = Modifier.fillMaxSize()) {
          SuccessState(movieState(), viewState.value.movieSections, eventsSender)
        }
      }
    }
  }
}


@Composable
@Suppress("LongMethod")
fun SuccessState(
  movie: Movie,
  movieSections: List<MovieDetailsViewModel.MovieDetailsSection>,
  eventsSender: SendChannel<Event>
) {
  ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
    val (poster, backButton, fab) = createRefs()

    movie.backdropPath?.let {
      MoviePoster(
        image = it,
        modifier = Modifier.constrainAs(poster) {
          top.linkTo(parent.top)
          start.linkTo(parent.start)
          end.linkTo(parent.end)
        }
      )
    }

    IconButton(
      onClick = { eventsSender.offer(Event.BackPressed) },
      modifier = Modifier
        .statusBarsPadding()
        .constrainAs(backButton) {
          top.linkTo(parent.top)
          start.linkTo(parent.start)
        },
    ) {
      Icon(asset = Icons.Default.ArrowBack)
    }

    FloatingActionButton(
      onClick = { eventsSender.offer(Event.PlayClicked) },
      modifier = Modifier.constrainAs(fab) {
        bottom.linkTo(parent.bottom)
        top.linkTo(parent.bottom)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
      },
    ) {
      Icon(asset = Icons.Filled.PlayArrow)
    }
  }

  MovieInfo(
    input = MovieInfoParams(
      title = movie.title,
      description = movie.getGenresString(),
      releaseDate = movie.releaseDate.date,
      country = movie.country,
      runtime = stringResource(
        id = R.string.movie_details_duration,
        formatArgs = arrayOf(movie.runtime)
      )
    ),
    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.movie_details_title_padding)),
  )

  if (!movie.overview.isNullOrEmpty()) {
    MovieOverview(text = movie.overview!!)
  }

  HorizontalList(
    items = movie.images,
    title = stringResource(id = R.string.movie_details_photos)
  ) { index, item ->
    MoviePhoto(
      image = item,
      modifier = getListItemPadding(index = index, totalSize = movie.images.size)
    )
  }

  Spacer(modifier = Modifier.padding(top = size(Size.Normal)))

  movieSections.find { it is MovieSimilar }?.let { section ->
    val similar = section as MovieSimilar

    HorizontalList(
      items = similar.movies,
      title = stringResource(id = R.string.movie_details_similar),
      listModifier = Modifier.navigationBarsPadding(),
    ) { index, item ->
      MovieCard(
        movie = item,
        width = dimensionResource(id = R.dimen.item_movie_width),
        height = dimensionResource(id = R.dimen.item_movie_height),
        modifier = getListItemPadding(index = index, totalSize = similar.movies.size),
      ) { movie ->
        eventsSender.offer(Event.MovieClicked(movie))
      }
    }
  }
}

private const val PROGRESS_SIZE = 40

@Composable
private fun ProgressState() {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    CircularProgressIndicator(
      modifier = Modifier.size(PROGRESS_SIZE.dp).gravity(Alignment.CenterHorizontally)
    )
  }
}

@Composable
private fun ErrorState() {
  // No-op
}