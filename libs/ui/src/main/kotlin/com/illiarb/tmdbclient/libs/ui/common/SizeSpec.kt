package com.illiarb.tmdbclient.libs.ui.common

import androidx.annotation.DimenRes

sealed class SizeSpec {

  object MatchParent : SizeSpec()

  object WrapContent : SizeSpec()

  class Fixed(@DimenRes val sizeRes: Int) : SizeSpec()
}