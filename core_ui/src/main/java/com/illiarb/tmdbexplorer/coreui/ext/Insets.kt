package com.illiarb.tmdbexplorer.coreui.ext

import android.view.View
import android.view.WindowInsets
import androidx.core.view.ViewCompat

// Grabbed from here:
// https://medium.com/androiddevelopers/windowinsets-listeners-to-layouts-8f9ccc8fa4d1

fun View.doOnApplyWindowInsets(f: (View, WindowInsets, InitialPadding) -> Unit) {
    // Create a snapshot of the view's padding state
    val initialPadding = recordInitialPaddingForView(this)
    // Set an actual OnApplyWindowInsetsListener which proxies to the given
    // lambda, also passing in the original padding state
    setOnApplyWindowInsetsListener { v, insets ->
        f(v, insets, initialPadding)
        // Always return the insets, so that children can also use them
        insets
    }
    // request some insets
    requestApplyInsetsWhenAttached()
}

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        // We're already attached, just request as normal
        requestApplyInsets()
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

fun View.awareOfWindowInsets() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
        setPadding(
            paddingLeft,
            paddingTop + insets.systemWindowInsetTop,
            paddingRight,
            paddingBottom
        )
        return@setOnApplyWindowInsetsListener insets
    }
}

private fun recordInitialPaddingForView(view: View) =
    InitialPadding(
        view.paddingLeft,
        view.paddingTop,
        view.paddingRight,
        view.paddingBottom
    )

data class InitialPadding(val left: Int, val top: Int, val right: Int, val bottom: Int)