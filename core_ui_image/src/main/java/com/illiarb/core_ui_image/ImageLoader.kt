package com.illiarb.core_ui_image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.illiarb.core_ui_image.blur.BlurTransformation

/**
 * @author ilya-rb on 29.12.18.
 */
fun ImageView.loadImage(
    url: String?,
    options: RequestOptions? = null,
    onResourceReady: (Drawable) -> Boolean = { false },
    onLoadError: (Throwable) -> Boolean = { false }
) {
    val request = Glide.with(context).load(url)

    options?.let {
        request.apply(mapOptions(context, it))

        if (it.useCrossFade) {
            request.transition(DrawableTransitionOptions.withCrossFade())
        }

        if (it.thumbnail != 0f) {
            request.thumbnail(it.thumbnail)
        }
    }

    val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            if (e != null) {
                return onLoadError(e)
            }
            return false
        }

        override fun onResourceReady(
            resource: Drawable,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            return onResourceReady(resource)
        }
    }

    request.listener(listener).into(this)
}

fun ImageView.clearImage() {
    Glide.with(context).clear(this)
}

private fun mapOptions(
    context: Context,
    options: RequestOptions
): com.bumptech.glide.request.RequestOptions {
    val result = com.bumptech.glide.request.RequestOptions()
    val transformations = mutableListOf<Transformation<Bitmap>>()

    options.blurParams?.let {
        transformations.add(BlurTransformation(context, it.radius, it.sampling))
    }

    options.cropOptions?.let {
        when (it) {
            CropOptions.CENTER_CROP -> transformations.add(CenterCrop())
            CropOptions.FIT_CENTER -> transformations.add(FitCenter())
            CropOptions.CENTER_INSIDE -> transformations.add(CenterInside())
            CropOptions.CIRCLE -> transformations.add(CircleCrop())
        }
    }

    if (options.cornerRadius > 0) {
        transformations.add(RoundedCorners(options.cornerRadius))
    }

    if (transformations.isNotEmpty()) {
        result.apply {
            transform(*transformations.toTypedArray())
        }
    }

    return result
}