package com.illiarb.tmdbclient.modules.details.v2

import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
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
import androidx.compose.foundation.lazy.LazyRowFor
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.libs.ui.v2.theme.Size
import com.illiarb.tmdbclient.libs.ui.v2.theme.size
import com.illiarb.tmdbclient.libs.util.Async
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
          SuccessState(movieState(), eventsSender)

          viewState.value.movieSections.find { it is MovieSimilar }?.let { section ->
            val similar = section as MovieSimilar

            HorizontalList(
              items = similar.movies,
              title = stringResource(id = R.string.movie_details_similar)
            ) {
              MovieCard(
                movie = it,
                width = dimensionResource(id = R.dimen.item_movie_width),
                height = dimensionResource(id = R.dimen.item_movie_height),
                modifier = Modifier.padding(horizontal = size(size = Size.Small)),
              ) { movie ->
                eventsSender.offer(Event.MovieClicked(movie))
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun SuccessState(movie: Movie, eventsSender: SendChannel<Event>) {
  ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
    val (poster, backButton, fab) = createRefs()

    movie.backdropPath?.let {
      TmdbImage(
        image = it,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .fillMaxWidth()
          .height(dimensionResource(id = R.dimen.movie_details_poster_height))
          .constrainAs(poster) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(parent.top)
          }
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
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        top.linkTo(poster.bottom)
        bottom.linkTo(poster.bottom)
      },
    ) {
      Icon(asset = Icons.Filled.PlayArrow)
    }
  }

  MovieInfo(
    modifier = Modifier.padding(top = size(Size.Normal)),
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
  )

  if (!movie.overview.isNullOrEmpty()) {
    MovieOverview(text = movie.overview!!)
  }

  HorizontalList(
    items = movie.images,
    title = stringResource(id = R.string.movie_details_photos)
  ) {
    MoviePhoto(
      image = it,
      modifier = Modifier.padding(
        horizontal = size(Size.Small)
      )
    )
  }
}

@Composable
private fun ProgressState() {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalGravity = Alignment.CenterHorizontally
  ) {
    CircularProgressIndicator(modifier = Modifier.size(40.dp).gravity(Alignment.CenterHorizontally))
  }
}

@Composable
private fun ErrorState() {
}

@Composable
private fun <T> HorizontalList(
  items: List<T>,
  title: String,
  itemContent: @Composable (T) -> Unit,
) {
  if (items.isEmpty()) return

  Text(
    text = title,
    style = MaterialTheme.typography.body1,
    modifier = Modifier.padding(start = size(Size.Normal)),
  )

  LazyRowFor(
    items = items,
    contentPadding = InnerPadding(size(Size.Normal)),
  ) {
    itemContent(it)
  }
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