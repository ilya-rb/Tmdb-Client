package com.illiarb.core_ui_recycler_view

sealed class LayoutType {

    data class Linear(val prefetchCount: Int = -1) : LayoutType()

    data class Grid(val spanCount: Int, val spaceBetween: Int = 0) : LayoutType()

}