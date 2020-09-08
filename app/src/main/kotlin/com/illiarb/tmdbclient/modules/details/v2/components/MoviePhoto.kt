package com.illiarb.tmdbclient.modules.details.v2.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.services.tmdb.domain.Image
import com.illiarb.tmdbclient.util.TmdbImage

@Composable
fun MoviePhoto(
  modifier: Modifier = Modifier,
  image: Image,
) {
  val imageModifier = modifier
    .height(dimensionResource(id = R.dimen.movie_details_item_photo_height))
    .width(dimensionResource(id = R.dimen.movie_details_item_photo_width))
    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_normal)))
    .drawShadow(elevation = dimensionResource(id = R.dimen.elevation_normal))

  TmdbImage(
    image = image,
    modifier = imageModifier,
    contentScale = ContentScale.Crop
  )
}