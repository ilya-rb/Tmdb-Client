package com.illiarb.tmdbexplorer.coreui.ext

import android.view.View
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.removeAdapterOnDetach() {
    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View?) = Unit
        override fun onViewDetachedFromWindow(v: View?) {
            // Find the best way to to leak the adapter
            adapter = null
        }
    })
}