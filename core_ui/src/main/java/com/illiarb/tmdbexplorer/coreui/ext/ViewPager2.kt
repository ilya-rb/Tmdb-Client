package com.illiarb.tmdbexplorer.coreui.ext

import androidx.viewpager2.widget.ViewPager2

fun ViewPager2.switchToNextPosition(cycle: Boolean = true, smoothScroll: Boolean = true) {
    adapter?.itemCount?.let { count ->
        if (currentItem < count - 1) {
            setCurrentItem(currentItem + 1, smoothScroll)
        } else if (cycle) {
            setCurrentItem(0, smoothScroll)
        }
    }
}