package com.illiarb.tmdbexplorer.coreui.ext

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes

/**
 * @author ilya-rb on 18.10.18.
 */
fun ViewGroup.inflate(@LayoutRes viewResId: Int): View = LayoutInflater.from(context).inflate(viewResId, this, false)

fun View.awareOfWindowInsetsWithMargin(@DimenRes space: Int) {
    setOnApplyWindowInsetsListener { _, insets ->
        val viewMargin = resources.getDimensionPixelSize(space)

        val params = layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = insets.systemWindowInsetTop + viewMargin

        layoutParams = params
        return@setOnApplyWindowInsetsListener insets
    }
}