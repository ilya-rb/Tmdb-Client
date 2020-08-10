package com.illiarb.tmdbclient.system

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.illiarb.tmdbclient.libs.ui.ext.isNightModeEnabled
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import javax.inject.Inject
import javax.inject.Singleton

interface DayNightModePreferences {

  val isNightModeEnabled: Boolean

  val nightModeChanged: Flow<Boolean>

  fun toggleDayNightMode()

  fun notifySystemNightModeChanged(isEnabled: Boolean)
}

@Singleton
class InMemoryDayNightModePreferences @Inject constructor(app: Application)
  : DayNightModePreferences {

  private val nightModeChanges = ConflatedBroadcastChannel(app.isNightModeEnabled())

  override val isNightModeEnabled: Boolean
    get() = nightModeChanges.value

  override val nightModeChanged: Flow<Boolean>
    get() = nightModeChanges.openSubscription().consumeAsFlow()

  override fun toggleDayNightMode() {
    val isEnabled = !nightModeChanges.value
    setDefaultNightMode(isEnabled)
    nightModeChanges.offer(isEnabled)
  }

  override fun notifySystemNightModeChanged(isEnabled: Boolean) {
    nightModeChanges.offer(isEnabled)
  }

  private fun setDefaultNightMode(enableNightMode: Boolean) {
    val defaultNightMode = if (enableNightMode) {
      AppCompatDelegate.MODE_NIGHT_YES
    } else {
      AppCompatDelegate.MODE_NIGHT_NO
    }

    if (AppCompatDelegate.getDefaultNightMode() != defaultNightMode) {
      AppCompatDelegate.setDefaultNightMode(defaultNightMode)
    }
  }
}