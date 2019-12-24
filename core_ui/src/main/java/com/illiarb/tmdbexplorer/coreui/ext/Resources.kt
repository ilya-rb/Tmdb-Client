package com.illiarb.tmdbexplorer.coreui.ext

import android.view.View
import androidx.annotation.DimenRes

fun View.dimen(@DimenRes id: Int): Int = resources.getDimensionPixelSize(id)