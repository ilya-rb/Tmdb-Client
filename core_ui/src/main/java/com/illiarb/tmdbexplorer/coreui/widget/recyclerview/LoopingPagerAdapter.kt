package com.illiarb.tmdbexplorer.coreui.widget.recyclerview

interface LoopingPagerAdapter {

    val realCount: Int

    fun getRealPosition(pos: Int) = pos % realCount

    companion object {
        const val MAX_COUNT = Int.MAX_VALUE
    }
}