package com.illiarb.tmdbclient.ui

import android.widget.ImageView
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.libs.imageloader.Image
import com.illiarb.tmdbclient.libs.imageloader.RequestOptions
import com.illiarb.tmdbclient.libs.imageloader.loadImage
import com.illiarb.tmdbclient.libs.ui.ext.doOnLayout
import com.illiarb.tmdbclient.services.tmdb.api.util.TmdbImageSizeSelector
import com.illiarb.tmdbclient.services.tmdb.api.domain.Image as TmdbImage

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