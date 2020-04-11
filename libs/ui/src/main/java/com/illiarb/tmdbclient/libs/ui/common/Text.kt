package com.illiarb.tmdbclient.libs.ui.common

import androidx.annotation.StringRes

sealed class Text {

  class AsString(val text: String) : Text()

  class AsResource(@StringRes val id: Int) : Text()
}