package com.illiarb.tmdbclient.modules.details.v2.components

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRowForIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.illiarb.tmdbclient.libs.ui.v2.theme.Size
import com.illiarb.tmdbclient.libs.ui.v2.theme.size

@Composable
fun <T> HorizontalList(
  listModifier: Modifier = Modifier,
  items: List<T>,
  title: String,
  itemContent: @Composable (Int, T) -> Unit,
) {
  if (items.isEmpty()) {
    return
  }

  Text(
    text = title,
    style = MaterialTheme.typography.body1,
    modifier = Modifier.padding(start = size(Size.Normal)),
  )

  LazyRowForIndexed(
    items = items,
    modifier = Modifier.padding(top = size(Size.Normal)).then(listModifier),
    itemContent = { index, item ->
      itemContent(index, item)
    }
  )
}

@Composable
fun getListItemPadding(index: Int, totalSize: Int): Modifier {
  return when (index) {
    0 -> {
      Modifier.padding(
        start = size(size = Size.Normal),
        end = size(size = Size.Small),
      )
    }
    totalSize - 1 -> {
      Modifier.padding(
        start = size(Size.Small),
        end = size(Size.Normal),
      )
    }
    else -> {
      Modifier.padding(
        start = size(Size.Small),
        end = size(Size.Small),
      )
    }
  }
}