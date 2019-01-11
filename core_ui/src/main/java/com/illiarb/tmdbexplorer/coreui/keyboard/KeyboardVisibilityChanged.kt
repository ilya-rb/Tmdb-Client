package com.illiarb.tmdbexplorer.coreui.keyboard

data class KeyboardVisibilityChanged(
    val visible: Boolean,
    val contentHeight: Int,
    val contentHeightBeforeResize: Int
)