package com.illiarb.tmdbclient.libs.ui.v2.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.illiarb.tmdbclient.libs.ui.R

@Composable
fun size(size: Size): Dp {
  return when (size) {
    Size.Normal -> dimensionResource(id = R.dimen.spacing_normal)
    Size.Small -> dimensionResource(id = R.dimen.spacing_small)
    Size.Micro -> dimensionResource(id = R.dimen.spacing_micro)
    Size.Large -> dimensionResource(id = R.dimen.spacing_large)
  }
}

enum class Size {
  Normal,
  Small,
  Micro,
  Large,
}