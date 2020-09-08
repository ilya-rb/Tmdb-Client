package com.illiarb.tmdbclient.modules.details.v2.components

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.libs.ui.v2.theme.Size
import com.illiarb.tmdbclient.libs.ui.v2.theme.size
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.util.TmdbImage

private const val TITLE_MAX_LINES = 2

@Composable
fun MovieCard(
  modifier: Modifier = Modifier,
  width: Dp,
  height: Dp,
  movie: Movie,
  onClick: (Movie) -> Unit
) {
  Column(modifier = modifier.width(width)) {
    movie.posterPath?.let {
      TmdbImage(
        image = it,
        modifier = Modifier
          .width(width)
          .height(height)
          .clip(RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_normal)))
          .drawShadow(elevation = dimensionResource(id = R.dimen.elevation_normal))
          .clickable(onClick = { onClick(movie) })
      )
    }

    Text(
      textAlign = TextAlign.Center,
      text = movie.title,
      style = MaterialTheme.typography.caption,
      maxLines = TITLE_MAX_LINES,
      modifier = Modifier.padding(top = size(Size.Small)).fillMaxWidth(),
    )
  }
}