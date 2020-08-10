package com.illiarb.tmdbclient.system

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.illiarb.tmdbclient.libs.logger.Logger
import javax.inject.Inject
import javax.inject.Singleton

interface DayNightModePreferences {

  val isNightModeEnabled: Boolean

  fun toggleDayNightMode()
}

@Singleton
class InMemoryDayNightModePreferences @Inject constructor(
  private val app: Application
) : DayNightModePreferences {

  private var _isNightModeEnabled = false

  override val isNightModeEnabled: Boolean
    get() = _isNightModeEnabled || isSystemNightModeEnabled()

  override fun toggleDayNightMode() {
    Logger.i("Night mode enabled: $_isNightModeEnabled")

    _isNightModeEnabled = !_isNightModeEnabled

    if (_isNightModeEnabled) {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    } else {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
  }

  private fun isSystemNightModeEnabled(): Boolean {
    return when (app.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
      Configuration.UI_MODE_NIGHT_YES -> true
      else -> {
        AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
      }
    }
  }
}