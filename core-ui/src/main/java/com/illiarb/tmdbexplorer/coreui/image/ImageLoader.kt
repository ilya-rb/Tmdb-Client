package com.illiarb.tmdbexplorer.coreui.image

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.illiarb.tmdbexplorer.coreui.BuildConfig

object ImageLoader {

    fun loadImage(
        target: ImageView,
        url: String,
        centerCrop: Boolean = false,
        cornerRadius: Int = 0
    ) {
        val transformations = mutableListOf<Transformation<Bitmap>>()

        if (centerCrop) {
            transformations.add(CenterCrop())
        }

        if (cornerRadius > 0) {
            transformations.add(RoundedCorners(cornerRadius))
        }

        Glide.with(target.context)
            .load(createImageUrl(url))
            .apply {
                if (transformations.isNotEmpty()) {
                    apply(RequestOptions().transforms(*transformations.toTypedArray()))
                }
            }
            .into(target)
    }

    fun clearImageView(target: ImageView) {
        Glide.with(target.context).clear(target)
    }

    private fun createImageUrl(url: String): String = "${BuildConfig.IMG_URL}/$url"
}