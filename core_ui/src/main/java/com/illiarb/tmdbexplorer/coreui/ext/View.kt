package com.illiarb.tmdbexplorer.coreui.ext

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce

/**
 * @author ilya-rb on 18.10.18.
 */
const val VIEW_DEBOUNCED_CLICKS_DELAY = 400L

fun ViewGroup.inflate(@LayoutRes viewResId: Int): View =
    LayoutInflater.from(context).inflate(viewResId, this, false)

fun View.awareOfWindowInsets() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val params = v.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = insets.systemWindowInsetTop
        return@setOnApplyWindowInsetsListener insets.consumeSystemWindowInsets()
    }
}

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun View.debouncedClicks(): Flow<Unit> = callbackFlow<Unit> {
    val clickListener = View.OnClickListener {
        offer(Unit)
    }

    setOnClickListener(clickListener)

    awaitClose {
        setOnClickListener(null)
    }
}.debounce(VIEW_DEBOUNCED_CLICKS_DELAY)

fun View.focusChanges(): Flow<Boolean> = callbackFlow {
    val focusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        offer(hasFocus)
    }

    onFocusChangeListener = focusChangeListener

    awaitClose {
        onFocusChangeListener = null
    }
}