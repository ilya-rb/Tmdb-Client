package com.illiarb.tmdbclient.libs.ui.common

import android.view.Window
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

class TranslucentStatusBarColorChanger(
  private val owner: LifecycleOwner,
  private val window: Window,
  private val targetColor: Int
) : LifecycleObserver {

  private val currentStatusBarColor: Int = window.statusBarColor

  init {
    owner.lifecycle.addObserver(this)
  }

  @Suppress("unused")
  @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
  fun onResume() {
    window.clearFlags(FLAG_TRANSLUCENT_STATUS or FLAG_TRANSLUCENT_NAVIGATION)
    window.statusBarColor = targetColor
  }

  @Suppress("unused")
  @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
  fun onPause() {
    window.addFlags(FLAG_TRANSLUCENT_STATUS or FLAG_TRANSLUCENT_NAVIGATION)
    window.statusBarColor = currentStatusBarColor
  }

  @Suppress("unused")
  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  fun onDestroy() {
    owner.lifecycle.removeObserver(this)
  }
}