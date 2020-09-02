package com.illiarb.tmdbclient.modules.details.v2

import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Stack
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.libs.util.Async
import com.illiarb.tmdbclient.modules.details.MovieDetailsViewModel.Event
import com.illiarb.tmdbclient.modules.details.MovieDetailsViewModel.State
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
        val movie = movieState()

        ScrollableColumn(modifier = Modifier.fillMaxSize()) {
          Stack(modifier = Modifier.fillMaxWidth()) {
            movie.backdropPath?.let {
              val imageModifier = Modifier
                .height(dimensionResource(id = R.dimen.movie_details_poster_height))
                .fillMaxWidth()

              TmdbImage(
                image = it,
                modifier = imageModifier,
                contentScale = ContentScale.Crop
              )
            }

            IconButton(
              modifier = Modifier.padding(top = dimensionResource(id = R.dimen.spacing_normal)),
              onClick = { eventsSender.offer(Event.BackPressed) }
            ) {
              Icon(asset = Icons.Default.ArrowBack)
            }

            FloatingActionButton(
              onClick = { eventsSender.offer(Event.PlayClicked) },
              modifier = Modifier.gravity(Alignment.BottomCenter),
            ) {
              Icon(asset = Icons.Filled.PlayArrow)
            }
          }

          MovieInfo(
            modifier = Modifier.padding(top = dimensionResource(id = R.dimen.spacing_normal)),
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

          movie.overview?.let {
            Text(
              text = it,
              style = MaterialTheme.typography.body1,
              textAlign = TextAlign.Center,
              modifier = Modifier.padding(dimensionResource(id = R.dimen.spacing_normal))
            )
          }
        }
      }
    }
  }
}

@Composable
fun ProgressState() {
  Row(
    modifier = Modifier.fillMaxSize(),
    verticalGravity = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center
  ) {
    CircularProgressIndicator(modifier = Modifier.size(40.dp))
  }
}

@Composable
fun ErrorState() {

}