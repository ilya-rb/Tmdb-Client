package com.illiarb.tmdbexplorer.coreui.image

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.illiarb.tmdbexplorer.coreui.BuildConfig
import com.illiarb.tmdbexplorer.coreui.image.blur.BlurTransformation
import java.io.File
import javax.inject.Inject

/**
 * @author ilya-rb on 29.12.18.
 */
class ImageLoader @Inject constructor() {

    fun fromUrl(url: String?, target: ImageView, options: RequestOptions? = null) {
        loadImageInternal(LoadData.Url(createImageUrl(url)), target, options)
    }

    fun fromFile(file: File, target: ImageView, options: RequestOptions? = null) {
        loadImageInternal(LoadData.File(file), target, options)
    }

    fun fromUri(uri: Uri, target: ImageView, options: RequestOptions? = null) {
        loadImageInternal(LoadData.Uri(uri), target, options)
    }

    fun clearTarget(target: ImageView) {
        Glide.with(target.context).clear(target)
    }

    private fun mapData(data: LoadData): Any? =
        when (data) {
            is LoadData.Url -> data.path
            is LoadData.File -> data.file
            is LoadData.Uri -> data.uri
        }

    private fun createImageUrl(url: String?): String =
        if (url != null) {
            "${BuildConfig.IMG_URL}/$url"
        } else {
            ""
        }

    private fun loadImageInternal(data: LoadData, target: ImageView, options: RequestOptions? = null) {
        val request = Glide.with(target.context).load(mapData(data))

        options?.let {
            request.apply(mapOptions(target.context, it))

            if (it.useCrossFade) {
                request.transition(DrawableTransitionOptions.withCrossFade())
            }

            if (it.thumbnail != 0f) {
                request.thumbnail(it.thumbnail)
            }
        }

        request.into(target)
    }

    private fun mapOptions(context: Context, options: RequestOptions): com.bumptech.glide.request.RequestOptions {
        val result = com.bumptech.glide.request.RequestOptions()
        val transformations = mutableListOf<Transformation<Bitmap>>()

        options.blurParams?.let {
            transformations.add(BlurTransformation(context, it.radius, it.sampling))
        }

        options.cropOptions?.let {
            when (it) {
                CropOptions.CENTER_CROP -> transformations.add(CenterCrop())
                CropOptions.FIT_CENTER -> transformations.add(FitCenter())
            }
        }

        if (options.cornerRadius > 0) {
            transformations.add(RoundedCorners(options.cornerRadius))
        }

        if (transformations.isNotEmpty()) {
            result.apply {
                transforms(*transformations.toTypedArray())
            }
        }

        return result
    }
}