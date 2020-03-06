package com.illiarb.coreuiimage

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.illiarb.tmdbexplorer.coreui.ext.doOnLayout
import com.illiarb.tmdblcient.core.domain.Image

/**
 * @author ilya-rb on 29.12.18.
 */
private val imageSizePattern = "w(\\d+)$".toRegex()

fun ImageView.loadImage(
  image: Image?,
  requestOptions: RequestOptions.() -> RequestOptions = { this }
) {
  if (image == null) {
    return
  }

  clear()
  doOnLayout {
    val selectedSize = selectSize(image.sizes, it.width) ?: return@doOnLayout
    val options = requestOptions(RequestOptions())
    val request = Glide.with(context)
      .load(image.buildFullUrl(selectedSize))
      .apply(mapOptions(options))
      .also { request ->
        if (options.useCrossFade) {
          request.transition(DrawableTransitionOptions.withCrossFade())
        }
      }
      .also { request ->
        if (options.thumbnail != 0f) {
          request.thumbnail(options.thumbnail)
        }
      }

    request.preload(it.width, it.height)
    request.listener(object : RequestListener<Drawable> {
      override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
      ): Boolean = false

      override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
      ): Boolean {
        options.onImageReady()
        return false
      }
    })

    request.into(this)
  }
}

fun ImageView.clear() {
  Glide.with(this).clear(this)
}

@Suppress("ComplexMethod")
private fun mapOptions(options: RequestOptions): com.bumptech.glide.request.RequestOptions {
  val result = com.bumptech.glide.request.RequestOptions()
  val transformations = mutableListOf<Transformation<Bitmap>>()

  options.cropOptions?.let {
    when (it) {
      CropOptions.CenterCrop -> transformations.add(CenterCrop())
      CropOptions.FitCenter -> transformations.add(FitCenter())
      CropOptions.CenterInside -> transformations.add(CenterInside())
      CropOptions.Circle -> transformations.add(CircleCrop())
    }
  }

  if (options.cornerRadius > 0) {
    transformations.add(RoundedCorners(options.cornerRadius))
  }

  if (transformations.isNotEmpty()) {
    result.apply {
      if (transformations.size == 1) {
        transform(transformations.first())
      } else {
        transform(MultiTransformation(transformations))
      }
    }
  }

  return result
}

/**
 * Source:
 * https://github.com/chrisbanes/tivi/blob/master/tmdb/src/main/java/app/tivi/tmdb/TmdbImageUrlProvider.kt
 */
@Suppress("ComplexMethod", "ReturnCount")
private fun selectSize(sizes: List<String>, imageWidth: Int): String? {
  var previousSize: String? = null
  var previousWidth = 0

  for (i in sizes.indices) {
    val size = sizes[i]
    val sizeWidth = extractWidthAsIntFrom(size) ?: continue

    if (sizeWidth > imageWidth) {
      if (previousSize != null && imageWidth > (previousWidth + sizeWidth) / 2) {
        return size
      } else if (previousSize != null) {
        return previousSize
      }
    } else if (i == sizes.size - 1) {
      // If we get here then we're larger than the last bucket
      if (imageWidth < sizeWidth * 2) {
        return size
      }
    }

    previousSize = size
    previousWidth = sizeWidth
  }

  return previousSize ?: if (sizes.isNotEmpty()) sizes.last() else null
}

private fun extractWidthAsIntFrom(size: String): Int? {
  return imageSizePattern.matchEntire(size)?.groups?.get(1)?.value?.toInt()
}
