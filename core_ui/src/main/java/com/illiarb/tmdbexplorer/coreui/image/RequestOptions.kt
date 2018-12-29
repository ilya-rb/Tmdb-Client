package com.illiarb.tmdbexplorer.coreui.image

import com.illiarb.tmdbexplorer.coreui.image.blur.BlurParams

/**
 * @author ilya-rb on 29.12.18.
 */
@Suppress("unused")
data class RequestOptions(
    var cropOptions: CropOptions? = null,
    var cornerRadius: Int = 0,
    var blurParams: BlurParams? = null
) {

    companion object {
        inline fun create(builder: RequestOptions.() -> RequestOptions) = builder(RequestOptions())
    }

    fun cropOptions(options: CropOptions) = apply { cropOptions = options }

    fun cornerRadius(radius: Int) = apply { cornerRadius = radius }

    fun blurParams(params: BlurParams) = apply { blurParams = params }
}