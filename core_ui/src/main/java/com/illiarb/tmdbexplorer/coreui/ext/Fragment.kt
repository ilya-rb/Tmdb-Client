package com.illiarb.tmdbexplorer.coreui.ext

import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment

fun Fragment.dimen(@DimenRes id: Int) = resources.getDimensionPixelSize(id)