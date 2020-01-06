package com.illiarb.coreuiimage.blur

class BlurParams(val radius: Int = DEFAULT_RADIUS, val sampling: Int = DEFAULT_DOWN_SAMPLING) {

    companion object {
        private const val DEFAULT_RADIUS = 3
        private const val DEFAULT_DOWN_SAMPLING = 15
    }
}