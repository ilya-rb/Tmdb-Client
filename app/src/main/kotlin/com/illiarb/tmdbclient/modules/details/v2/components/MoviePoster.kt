package com.illiarb.tmdbclient.modules.details.v2.components

import androidx.compose.foundation.gestures.ZoomableController
import androidx.compose.foundation.gestures.zoomable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.drawLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.AnimationClockAmbient
import androidx.compose.ui.res.dimensionResource
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.modules.details.v2.shape.MoviePosterShape
import com.illiarb.tmdbclient.services.tmdb.domain.Image
import com.illiarb.tmdbclient.util.TmdbImage

private const val POSTER_BOTTOM_SHAPE_OFFSET = 300f

@Composable
fun MoviePoster(
  modifier: Modifier = Modifier,
  image: Image,
) {
  val imageScale = remember { mutableStateOf(1f) }

  TmdbImage(
    image = image,
    contentScale = ContentScale.Crop,
    modifier = Modifier
      .fillMaxWidth()
      .height(dimensionResource(id = R.dimen.movie_details_poster_height))
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
      .clip(MoviePosterShape(offset = POSTER_BOTTOM_SHAPE_OFFSET))
      .then(modifier)
  )
}