package com.illiarb.tmdbclient.modules.details.v2

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.illiarb.tmdbclient.R

data class MovieInfoParams(
  val title: String,
  val description: String?,
  val releaseDate: String?,
  val country: String?,
  val runtime: String?
)

@Composable
fun MovieInfo(
  input: MovieInfoParams,
  modifier: Modifier = Modifier
) {
  Column(modifier = Modifier.fillMaxWidth() + modifier) {
    Text(
      text = input.title,
      style = MaterialTheme.typography.h5,
      color = MaterialTheme.colors.secondary,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.fillMaxWidth(),
      textAlign = TextAlign.Center
    )

    if (!input.description.isNullOrEmpty()) {
      Text(
        text = input.description,
        modifier = Modifier.fillMaxWidth()
          .padding(top = dimensionResource(id = R.dimen.spacing_normal)),
        textAlign = TextAlign.Center,
      )
    }

    Row(
      modifier = Modifier.fillMaxWidth()
        .padding(top = dimensionResource(id = R.dimen.spacing_normal)),
      horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
      Text(
        text = stringResource(id = R.string.movie_details_year),
        color = MaterialTheme.colors.secondary,
        style = MaterialTheme.typography.body1,
      )
      Text(
        text = stringResource(id = R.string.movie_details_country),
        color = MaterialTheme.colors.secondary,
        style = MaterialTheme.typography.body1,
      )
      Text(
        text = stringResource(id = R.string.movie_details_length),
        color = MaterialTheme.colors.secondary,
        style = MaterialTheme.typography.body1,
      )
    }

    Row(
      modifier = Modifier.fillMaxWidth()
        .padding(top = dimensionResource(id = R.dimen.spacing_normal)),
      horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
      if (!input.releaseDate.isNullOrEmpty()) {
        Text(
          text = input.releaseDate,
          style = MaterialTheme.typography.caption,
        )
      }

      if (!input.country.isNullOrEmpty()) {
        Text(
          text = input.country,
          style = MaterialTheme.typography.caption,
        )
      }

      if (!input.runtime.isNullOrEmpty()) {
        Text(
          text = input.runtime,
          style = MaterialTheme.typography.caption,
        )
      }
    }
  }
}