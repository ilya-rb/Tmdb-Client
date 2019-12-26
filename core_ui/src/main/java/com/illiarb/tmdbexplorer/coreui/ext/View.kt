package com.illiarb.tmdbexplorer.coreui.ext

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce

/**
 * @author ilya-rb on 18.10.18.
 */
private const val VIEW_DEBOUNCED_CLICKS_DELAY = 400L

fun View.awareOfWindowInsets() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
        setPadding(
            paddingLeft,
            paddingTop + insets.systemWindowInsetTop,
            paddingRight,
            paddingBottom
        )
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

fun View.debouncedClicks(): Flow<Unit> = callbackFlow {
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