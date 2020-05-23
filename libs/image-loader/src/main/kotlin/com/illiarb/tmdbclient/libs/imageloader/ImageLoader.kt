package com.illiarb.tmdbclient.libs.imageloader

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

/**
 * @author ilya-rb on 29.12.18.
 */
fun ImageView.loadImage(
  image: Image,
  requestOptions: RequestOptions.() -> RequestOptions = { this }
) {
  val options = requestOptions(RequestOptions())

  Glide.with(context)
    .load(image.asGlideResource())
    .apply(mapOptions(options))
    .error(options.errorRes)
    .also { request ->
      if (options.useCrossFade) {
        request.transition(DrawableTransitionOptions.withCrossFade())
      }
      if (options.thumbnail != 0f) {
        request.thumbnail(options.thumbnail)
      }
    }
    .listener(object : RequestListener<Drawable> {
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
    .into(this)
}

fun ImageView.clear() {
  Glide.with(this).clear(this)
}

private fun Image.asGlideResource(): Any {
  return when (this) {
    is Image.Network -> this.url
    is Image.File -> this.file
    is Image.Resource -> this.resId
    is Image.Uri -> this.uri
  }
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

