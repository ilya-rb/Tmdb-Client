package com.illiarb.tmdbexplorer.coreui.image.blur

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.renderscript.*

internal object RsBlur {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Throws(RSRuntimeException::class)
    fun blur(context: Context, bitmap: Bitmap, radius: Int): Bitmap {
        var rs: RenderScript? = null
        var input: Allocation? = null
        var output: Allocation? = null
        val blur: ScriptIntrinsicBlur? = null

        try {
            rs = RenderScript.create(context)
            rs?.let { renderScript ->
                renderScript.messageHandler = RenderScript.RSMessageHandler()
                input = Allocation.createFromBitmap(
                    renderScript,
                    bitmap,
                    Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT
                )

                output = Allocation.createTyped(renderScript, input!!.type)

                ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
                    .apply {
                        setInput(input)
                        setRadius(radius.toFloat())
                        forEach(output)
                    }

                output?.copyTo(bitmap)
            }
        } finally {
            if (rs != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    RenderScript.releaseAllContexts()
                } else {
                    rs.destroy()
                }
            }

            blur?.destroy()
            input?.destroy()
            output?.destroy()
        }

        return bitmap
    }
}