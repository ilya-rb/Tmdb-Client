package com.illiarb.tmdbclient.modules.home.delegates.nowplaying

import android.content.Context
import android.view.WindowManager
import androidx.core.content.getSystemService
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

@Suppress("MagicNumber")
class ProgressUpdateTimer(
  context: Context,
  max: Int,
  private val onProgressUpdated: ProgressUpdateTimer.(Int) -> Unit
) {

  companion object {
    const val DEFAULT_REFRESH_RATE = 60f
  }

  private val progressUpdatePercent = max / 100
  private val timerUpdateValue = progressUpdatePercent / getTimerUpdatePeriod(context)
  private var timer: Timer? = null

  fun resetTimer() {
    timer?.cancel()
    timer = fixedRateTimer(
      initialDelay = 0L,
      period = timerUpdateValue.toLong()
    ) {
      onProgressUpdated(timerUpdateValue)
    }
  }

  fun cancelTimer() {
    timer?.cancel()
    timer = null
  }

  private fun getTimerUpdatePeriod(context: Context): Int {
    val refreshRate = context.getSystemService<WindowManager>()
      ?.defaultDisplay
      ?.refreshRate
      ?: DEFAULT_REFRESH_RATE

    return 1000 / refreshRate.toInt()
  }
}