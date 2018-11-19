package com.illiarb.tmdbexplorer.coreui.image

class BlurParams(val radius: Int = DEFAULT_DOWN_SAMPLING, val sampling: Int = MAX_RADIUS) {

    companion object {
        private const val MAX_RADIUS = 15
        private const val DEFAULT_DOWN_SAMPLING = 3
    }
}