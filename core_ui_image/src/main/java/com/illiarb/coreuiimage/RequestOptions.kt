package com.illiarb.coreuiimage

import androidx.annotation.FloatRange
import com.illiarb.coreuiimage.blur.BlurParams

/**
 * @author ilya-rb on 29.12.18.
 */
@Suppress("unused")
data class RequestOptions(
    var cropOptions: CropOptions? = null,
    var cornerRadius: Int = 0,
    var blurParams: BlurParams? = null,
    var thumbnail: Float = 0f,
    var useCrossFade: Boolean = true
) {

    fun thumbnail(@FloatRange(from = 0.1, to = 0.9) fraction: Float) = apply {
        thumbnail = fraction
    }

    fun crop(options: CropOptions) = apply {
        cropOptions = options
    }

    fun cornerRadius(radius: Int) = apply {
        cornerRadius = radius
    }

    fun blur(params: BlurParams) = apply {
        blurParams = params
    }

    fun crossFade(use: Boolean) = apply {
        useCrossFade = use
    }
}
