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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.libs.ui.v2.theme.Size
import com.illiarb.tmdbclient.libs.ui.v2.theme.size

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
        modifier = Modifier.fillMaxWidth().padding(top = size(Size.Normal)),
        textAlign = TextAlign.Center,
      )
    }

    LabelsRow(input)

    ValuesRow(input)
  }
}

@Composable
private fun LabelsRow(input: MovieInfoParams) {
  Row(
    modifier = Modifier.fillMaxWidth().padding(top = size(Size.Normal)),
    horizontalArrangement = Arrangement.SpaceEvenly,
  ) {
    if (input.showReleaseDate) {
      Text(
        text = stringResource(id = R.string.movie_details_year),
        color = MaterialTheme.colors.secondary,
        style = MaterialTheme.typography.body1,
      )
    }

    if (input.showCountry) {
      Text(
        text = stringResource(id = R.string.movie_details_country),
        color = MaterialTheme.colors.secondary,
        style = MaterialTheme.typography.body1,
      )
    }

    if (input.showRuntime) {
      Text(
        text = stringResource(id = R.string.movie_details_length),
        color = MaterialTheme.colors.secondary,
        style = MaterialTheme.typography.body1,
      )
    }
  }
}

@Composable
private fun ValuesRow(input: MovieInfoParams) {
  Row(
    modifier = Modifier.fillMaxWidth().padding(top = size(Size.Small)),
    horizontalArrangement = Arrangement.SpaceEvenly,
  ) {
    if (input.showReleaseDate) {
      Text(
        text = input.releaseDate!!,
        style = MaterialTheme.typography.caption,
      )
    }

    if (input.showCountry) {
      Text(
        text = input.country!!,
        style = MaterialTheme.typography.caption,
      )
    }

    if (input.showRuntime) {
      Text(
        text = input.runtime!!,
        style = MaterialTheme.typography.caption,
      )
    }
  }
}

data class MovieInfoParams(
  val title: String,
  val description: String?,
  val releaseDate: String?,
  val country: String?,
  val runtime: String?
) {

  val showReleaseDate: Boolean
    get() = !releaseDate.isNullOrEmpty()

  val showCountry: Boolean
    get() = !country.isNullOrEmpty()

  val showRuntime: Boolean
    get() = !runtime.isNullOrEmpty()
}
