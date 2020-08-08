package com.illiarb.tmdbclient.libs.imageloader

import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange

/**
 * @author ilya-rb on 29.12.18.
 */
data class RequestOptions(
  var cropOptions: CropOptions? = null,
  var cornerRadius: Int = 0,
  var thumbnail: Float = 0f,
  var errorRes: Int = 0,
  var useCrossFade: Boolean = true,
  var onImageReady: () -> Unit = {}
) {

  fun errorRes(@DrawableRes idRes: Int) = apply {
    errorRes = idRes
  }

  fun thumbnail(@FloatRange(from = 0.1, to = 0.9) fraction: Float) = apply {
    thumbnail = fraction
  }

  fun onImageReady(callback: () -> Unit) = apply {
    onImageReady = callback
  }

  fun crop(options: CropOptions) = apply {
    cropOptions = options
  }

  fun cornerRadius(radius: Int) = apply {
    cornerRadius = radius
  }

  fun crossFade(use: Boolean) = apply {
    useCrossFade = use
  }
}
