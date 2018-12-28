package com.illiarb.tmdbexplorer.coreui.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.illiarb.tmdbexplorer.coreui.BuildConfig
import com.illiarb.tmdbexplorer.coreui.R
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

object ImageLoader {

    // TODO: Move all params to request builder an reuse
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

    fun extractColorFromImage(context: Context, path: String): Single<Int> =
        Single.create<Int> { emitter ->
            val request = Glide.with(context)
                .asBitmap()
                .load(createImageUrl(path))
                .submit()

            val bitmap = request.get()
            val palette = Palette.from(bitmap).generate()

            if (!emitter.isDisposed) {
                emitter.onSuccess(palette.getLightMutedColor(Color.WHITE))
            }

            emitter.setCancellable {
                if (!request.isCancelled) {
                    request.cancel(false)
                }
            }
        }
            .subscribeOn(Schedulers.io())

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