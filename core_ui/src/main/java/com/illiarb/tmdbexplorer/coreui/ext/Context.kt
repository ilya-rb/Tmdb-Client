package com.illiarb.tmdbexplorer.coreui.ext

import android.content.Context
import android.view.View
import androidx.annotation.DimenRes

/**
 * @author ilya-rb on 06.11.18.
 */
fun Context.getStatusBarHeight(@DimenRes fallbackRes: Int = View.NO_ID): Int {
    var result = 0

    // Probably need to investigate how
    // to set window insets correctly instead of this
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")

    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    } else if (fallbackRes != View.NO_ID) {
        result = resources.getDimensionPixelSize(fallbackRes)
    }

    return result
}