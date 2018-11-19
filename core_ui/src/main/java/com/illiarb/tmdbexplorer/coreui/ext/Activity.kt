package com.illiarb.tmdbexplorer.coreui.ext

import android.app.Activity
import android.view.WindowManager

/**
 * @author ilya-rb on 18.10.18.
 */
fun Activity.setTranslucentStatusBar(set: Boolean) {
    if (set) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    } else {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}