package com.illiarb.coreuiimage

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.illiarb.coreuiimage.blur.BlurTransformation
import com.illiarb.tmdblcient.core.domain.Image

/**
 * @author ilya-rb on 29.12.18.
 */

private val imageSizePattern = "w(\\d+)$".toRegex()

fun ImageView.loadImage(image: Image?, options: RequestOptions? = null) {
    if (image == null) {
        return
    }

    val selectedSize = selectSize(image.sizes, width)
    val request = Glide.with(context).load(image.buildFullUrl(selectedSize))

    options?.let {
        request.apply(mapOptions(context, it))

        if (it.useCrossFade) {
            request.transition(DrawableTransitionOptions.withCrossFade())
        }

        if (it.thumbnail != 0f) {
            request.thumbnail(it.thumbnail)
        }
    }

    request.into(this)
}

@Suppress("ComplexMethod")
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
            transformations.forEach { transform(it) }
        }
    }

    return result
}

/**
 * Source:
 * https://github.com/chrisbanes/tivi/blob/master/tmdb/src/main/java/app/tivi/tmdb/TmdbImageUrlProvider.kt
 */
@Suppress("ComplexMethod", "ReturnCount")
private fun selectSize(sizes: List<String>, imageWidth: Int): String {
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

    return previousSize ?: sizes.last()
}

private fun extractWidthAsIntFrom(size: String): Int? {
    return imageSizePattern.matchEntire(size)?.groups?.get(1)?.value?.toInt()
}
