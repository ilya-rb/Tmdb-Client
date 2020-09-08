package com.illiarb.tmdbclient.modules.details.v2

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.ColumnScope.gravity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.libs.ui.v2.theme.Size
import com.illiarb.tmdbclient.libs.ui.v2.theme.size

private const val OVERVIEW_MAX_LINES = 3

@Composable
internal fun MovieOverview(text: String) {
  val wasMeasured = remember { mutableStateOf(false) }
  val didOverflowHeight = remember { mutableStateOf(false) }
  val isExpanded = remember { mutableStateOf(false) }
  val spacing = size(Size.Normal)

  Text(
    text = text,
    style = MaterialTheme.typography.body1,
    textAlign = TextAlign.Center,
    modifier = Modifier.padding(
      start = spacing,
      end = spacing,
      top = spacing,
    ),
    maxLines = if (isExpanded.value) Integer.MAX_VALUE else OVERVIEW_MAX_LINES,
    overflow = TextOverflow.Ellipsis,
    onTextLayout = { result ->
      if (!wasMeasured.value) {
        wasMeasured.value = true
        didOverflowHeight.value = result.didOverflowHeight
      }
    }
  )

  if (didOverflowHeight.value) {
    TextButton(
      shape = MaterialTheme.shapes.medium,
      onClick = { isExpanded.value = !isExpanded.value },
      modifier = Modifier
        .gravity(Alignment.End)
        .padding(end = size(Size.Small)),
    ) {
      Text(
        text = if (isExpanded.value) {
          stringResource(id = R.string.movie_details_collapse_overview)
        } else {
          stringResource(id = R.string.movie_details_expand_overview)
        }
      )
    }
  }
}