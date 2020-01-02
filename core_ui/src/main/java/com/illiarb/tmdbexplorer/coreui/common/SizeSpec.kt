package com.illiarb.tmdbexplorer.coreui.common

import androidx.annotation.DimenRes

sealed class SizeSpec {
    object MatchParent : SizeSpec()
    object WrapContent : SizeSpec()
    class Fixed(@DimenRes val sizeRes: Int) : SizeSpec()
}