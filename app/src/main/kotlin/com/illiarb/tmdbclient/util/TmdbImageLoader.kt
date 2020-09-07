package com.illiarb.tmdbclient.util

import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.WithConstraints
import androidx.compose.ui.layout.ContentScale
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.libs.imageloader.ComposeGlideImage
import com.illiarb.tmdbclient.libs.imageloader.Image
import com.illiarb.tmdbclient.libs.imageloader.RequestOptions
import com.illiarb.tmdbclient.libs.imageloader.loadImage
import com.illiarb.tmdbclient.libs.ui.ext.doOnLayout
import com.illiarb.tmdbclient.services.tmdb.util.TmdbImageSizeSelector
import com.illiarb.tmdbclient.services.tmdb.domain.Image as TmdbImage

private const val SIZE_ORIGINAL = Integer.MIN_VALUE

fun ImageView.loadTmdbImage(
  image: TmdbImage?,
  width: Int = 0,
  height: Int = 0,
  requestOptions: RequestOptions.() -> RequestOptions = { this }
) {
  if (image == null || image.path.isEmpty()) {
    loadImage(Image.Resource(R.drawable.ic_image_placeholder))
    return
  }

  if (width != 0 && height != 0) {
    val selectedSize = TmdbImageSizeSelector.selectSize(
      image.sizes,
      width,
      height
    )
    selectedSize?.let { size ->
      loadImage(Image.Network(image.buildFullUrl(size)), requestOptions)
    }
  } else {
    doOnLayout {
      val selectedSize = TmdbImageSizeSelector.selectSize(
        image.sizes,
        it.width,
        it.height
      )
      selectedSize?.let { size ->
        loadImage(Image.Network(image.buildFullUrl(size)), requestOptions)
      }
    }
  }
}

@Composable
fun TmdbImage(
  image: TmdbImage,
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.None,
  requestOptions: RequestOptions.() -> RequestOptions = { this }
) {
  WithConstraints {
    val width =
      if (constraints.maxWidth > 0 && constraints.maxWidth < Int.MAX_VALUE) {
        constraints.maxWidth
      } else {
        SIZE_ORIGINAL
      }

    val height =
      if (constraints.maxHeight > 0 && constraints.maxHeight < Int.MAX_VALUE) {
        constraints.maxHeight
      } else {
        SIZE_ORIGINAL
      }

    val selectedSize = TmdbImageSizeSelector.selectSize(
      image.sizes,
      width,
      height
    )

    ComposeGlideImage(
      image = Image.Network(image.buildFullUrl(selectedSize ?: "")),
      width = width,
      height = height,
      modifier = modifier,
      contentScale = contentScale,
      requestOptions = requestOptions,
    )
  }
}