package com.illiarb.coreuiimage.blur

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.NonNull
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class BlurTransformation(
    private val context: Context,
    private val radius: Int,
    private val sampling: Int
) : BitmapTransformation() {

    companion object {
        private const val VERSION = 1
        private const val ID = "jp.wasabeef.glide.transformations.BlurTransformation.$VERSION"
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val width = toTransform.width
        val height = toTransform.height

        val scaledWidth = width / sampling
        val scaledHeight = height / sampling

        var bitmap = pool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        canvas.scale(1 / sampling.toFloat(), 1 / sampling.toFloat())

        val paint = Paint()
        paint.flags = Paint.FILTER_BITMAP_FLAG

        canvas.drawBitmap(toTransform, 0f, 0f, paint)

        bitmap = RsBlur.blur(context, bitmap, radius)
        return bitmap
    }

    override fun toString(): String = "BlurTransformation(radius=$radius, sampling=$sampling)"

    override fun equals(other: Any?): Boolean =
        other is BlurTransformation && other.radius == radius && other.sampling == sampling

    override fun hashCode(): Int = ID.hashCode() + radius * 1000 + sampling * 10

    override fun updateDiskCacheKey(@NonNull messageDigest: MessageDigest) =
        messageDigest.update((ID + radius + sampling).toByteArray(Key.CHARSET))
}