package com.illiarb.tmdbexplorer.coreui.image

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.illiarb.tmdbexplorer.coreui.BuildConfig
import com.illiarb.tmdbexplorer.coreui.R

object ImageLoader {

    fun loadImage(
        target: ImageView,
        url: String?,
        centerCrop: Boolean = false,
        cornerRadius: Int = 0,
        blur: BlurParams? = null
    ) {
        val transformations = mutableListOf<Transformation<Bitmap>>()

        blur?.let {
            transformations.add(BlurTransformation(target.context, it.radius, it.sampling))
        }

        if (centerCrop) {
            transformations.add(CenterCrop())
        }

        if (cornerRadius > 0) {
            transformations.add(RoundedCorners(cornerRadius))
        }

        Glide.with(target.context)
            .load(createImageUrl(url))
            .apply {
                val options = RequestOptions()
                    .apply {
                        error(R.drawable.image_error_placeholder)

                        if (transformations.isNotEmpty()) {
                            transforms(*transformations.toTypedArray())
                        }
                    }
                apply(options)
            }
            .into(target)
    }

    fun clearImageView(target: ImageView) {
        Glide.with(target.context).clear(target)
    }

    private fun createImageUrl(url: String?): String =
        if (url != null) {
            "${BuildConfig.IMG_URL}/$url"
        } else {
            ""
        }
}