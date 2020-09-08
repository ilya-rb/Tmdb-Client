package com.illiarb.tmdbclient.modules.details.v2

import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.gestures.ZoomableController
import androidx.compose.foundation.gestures.zoomable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.InnerPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRowForIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.drawLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.AnimationClockAmbient
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
import com.illiarb.tmdbclient.services.tmdb.domain.Image
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.util.TmdbImage
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
    val imageScale = remember { mutableStateOf(1f) }

    movie.backdropPath?.let {
      TmdbImage(
        image = it,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .fillMaxWidth()
          .height(dimensionResource(id = R.dimen.movie_details_poster_height))
          .constrainAs(poster) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
          }
          .zoomable(
            controller = ZoomableController(
              animationClock = AnimationClockAmbient.current
            ) { value ->
              imageScale.value = imageScale.value * value
            },
            onZoomStopped = {
              imageScale.value = 1f
            }
          )
          .drawLayer(
            scaleX = imageScale.value,
            scaleY = imageScale.value,
          )
          .clip(
            MoviePosterShape(
              offset = dimensionResource(id = R.dimen.movie_details_curve_offset).value
            )
          )
      )
    }

    val imageMargin = size(Size.Normal)

    IconButton(
      onClick = { eventsSender.offer(Event.BackPressed) },
      modifier = Modifier.constrainAs(backButton) {
        top.linkTo(parent.top, margin = imageMargin)
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
    modifier = Modifier.padding(top = size(Size.Large))
  )

  if (!movie.overview.isNullOrEmpty()) {
    MovieOverview(text = movie.overview!!)
  }

  HorizontalList(
    items = movie.images,
    title = stringResource(id = R.string.movie_details_photos)
  ) { index, item ->
    val padding = if (index == 0 || index == movie.images.size - 1) 0.dp else size(Size.Small)

    MoviePhoto(
      image = item,
      modifier = Modifier.padding(horizontal = padding)
    )
  }

  movieSections.find { it is MovieSimilar }?.let { section ->
    val similar = section as MovieSimilar
    MovieSimilarSection(movies = similar.movies, eventsSender = eventsSender)
  }
}

private const val PROGRESS_SIZE = 40

@Composable
private fun ProgressState() {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalGravity = Alignment.CenterHorizontally
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

@Composable
private fun <T> HorizontalList(
  items: List<T>,
  title: String,
  itemContent: @Composable (Int, T) -> Unit,
) {
  if (items.isEmpty()) return

  Text(
    text = title,
    style = MaterialTheme.typography.body1,
    modifier = Modifier.padding(start = size(Size.Normal)),
  )

  LazyRowForIndexed(
    items = items,
    contentPadding = InnerPadding(size(Size.Normal)),
    itemContent = { index, item ->
      itemContent(index, item)
    }
  )
}

@Composable
private fun MoviePhoto(
  modifier: Modifier = Modifier,
  image: Image,
) {
  val imageModifier = modifier
    .height(dimensionResource(id = R.dimen.movie_details_item_photo_height))
    .width(dimensionResource(id = R.dimen.movie_details_item_photo_width))
    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_normal)))

  TmdbImage(
    image = image,
    modifier = imageModifier,
    contentScale = ContentScale.Crop
  )
}

@Composable
private fun MovieSimilarSection(
  movies: List<Movie>,
  eventsSender: SendChannel<Event>,
) {
  HorizontalList(
    items = movies,
    title = stringResource(id = R.string.movie_details_similar)
  ) { index, item ->
    val padding = if (index == 0 || index == movies.size - 1) {
      0.dp
    } else {
      size(size = Size.Small)
    }

    MovieCard(
      movie = item,
      width = dimensionResource(id = R.dimen.item_movie_width),
      height = dimensionResource(id = R.dimen.item_movie_height),
      modifier = Modifier.padding(horizontal = padding),
    ) { movie ->
      eventsSender.offer(Event.MovieClicked(movie))
    }
  }
}