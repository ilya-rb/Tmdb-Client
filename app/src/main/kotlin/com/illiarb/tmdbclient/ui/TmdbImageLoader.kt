package com.illiarb.tmdbclient.ui

import android.widget.ImageView
import com.illiarb.tmdbclient.libs.imageloader.Image
import com.illiarb.tmdbclient.libs.imageloader.RequestOptions
import com.illiarb.tmdbclient.libs.imageloader.loadImage
import com.illiarb.tmdbclient.libs.ui.ext.doOnLayout
import com.illiarb.tmdbclient.services.tmdb.util.TmdbImageSizeSelector
import com.illiarb.tmdbclient.services.tmdb.domain.Image as TmdbImage

fun ImageView.loadTmdbImage(
  image: TmdbImage?,
  requestOptions: RequestOptions.() -> RequestOptions = { this }
) {
  if (image == null) {
    return
  }

  doOnLayout {
    val selectedSize = TmdbImageSizeSelector.selectSize(
      image.sizes,
      it.width,
      it.height
    ) ?: return@doOnLayout

    loadImage(Image.Network(image.buildFullUrl(selectedSize)), requestOptions)
  }
}