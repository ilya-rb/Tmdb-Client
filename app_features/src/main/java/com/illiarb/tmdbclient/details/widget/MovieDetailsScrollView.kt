package com.illiarb.tmdbclient.details.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ScrollView
import kotlin.math.abs

class MovieDetailsScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {

    private var _scrollInterceptor: ScrollInterceptor? = null

    var scrollInterceptor: ScrollInterceptor?
        get() = _scrollInterceptor
        set(value) {
            _scrollInterceptor = value
        }

    init {
        viewTreeObserver.addOnScrollChangedListener {
            val overScroll = top - scrollY
            if (overScroll < 0) {
                scrollInterceptor?.onOverScrollDetected(abs(overScroll))
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean =
        if (scrollY > 0 || scrollInterceptor?.isAllowedToScroll() == true) super.onTouchEvent(ev) else false

    interface ScrollInterceptor {

        fun isAllowedToScroll(): Boolean

        fun onOverScrollDetected(overScroll: Int)
    }
}