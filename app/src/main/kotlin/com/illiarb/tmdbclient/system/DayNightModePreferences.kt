package com.illiarb.tmdbclient.system

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.PowerManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.getSystemService
import com.illiarb.tmdbclient.libs.ui.ext.isNightModeEnabled
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import javax.inject.Inject
import javax.inject.Singleton

interface DayNightModePreferences {

  /**
   * Get user preference of night mode synchronously
   */
  val isNightModeEnabled: Boolean

  /**
   * Flow for observing updates of user preference of night mode
   */
  val nightModeChanged: Flow<Boolean>

  /**
   * Change app day night mode
   */
  fun toggleDayNightMode()
}

interface DayNightModeChangeNotifier {

  /**
   * Interface for notifying day night preference when dark theme
   * is applied system-wide
   */
  fun notifySystemNightModeChanged(isEnabled: Boolean)
}

@Singleton
class InMemoryDayNightModePreferences @Inject constructor(
  private val app: Application
) : DayNightModePreferences, DayNightModeChangeNotifier {

  companion object {
    const val ACTION_POWER_SAVE_MODE_CHANGED = "android.os.action.POWER_SAVE_MODE_CHANGED"
  }

  private val nightModeEnabled = ConflatedBroadcastChannel(app.isNightModeEnabled())

  override val isNightModeEnabled: Boolean
    get() = nightModeEnabled.value

  override val nightModeChanged: Flow<Boolean>
    get() = nightModeEnabled.openSubscription().consumeAsFlow()

  init {
    app.registerReceiver(
      object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
          val isPowerSaveMode = app.getSystemService<PowerManager>()?.isPowerSaveMode == true
          if (isPowerSaveMode) {
            nightModeEnabled.offer(true)
            setDefaultNightMode(true)
          }
        }
      },
      IntentFilter(ACTION_POWER_SAVE_MODE_CHANGED)
    )
  }

  override fun toggleDayNightMode() {
    val isEnabled = !nightModeEnabled.value
    setDefaultNightMode(isEnabled)
    nightModeEnabled.offer(isEnabled)
  }

  override fun notifySystemNightModeChanged(isEnabled: Boolean) {
    nightModeEnabled.offer(isEnabled)
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