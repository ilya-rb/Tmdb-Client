package com.illiarb.tmdbclient.system

import androidx.appcompat.app.AppCompatDelegate
import javax.inject.Inject

interface DayNightModePreferences {

  val isNightModeEnabled: Boolean

  fun toggleDayNightMode()
}

class DefaultDayNightModePreferences @Inject constructor() : DayNightModePreferences {

  private var _isNightModeEnabled = false

  override val isNightModeEnabled: Boolean = _isNightModeEnabled

  override fun toggleDayNightMode() {
    _isNightModeEnabled = !_isNightModeEnabled

    if (_isNightModeEnabled) {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    } else {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
  }
}