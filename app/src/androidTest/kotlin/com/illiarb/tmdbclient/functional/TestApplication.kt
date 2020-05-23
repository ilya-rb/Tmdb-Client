package com.illiarb.tmdbclient.functional

import android.app.Application
import androidx.core.provider.FontRequest
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig
import com.illiarb.tmdbclient.di.AppInjector
import com.illiarb.tmdbclient.libs.ui.R

class TestApplication : Application() {

  private val testAppComponent = TestAppComponent(this)

  override fun onCreate() {
    super.onCreate()
    AppInjector(this, testAppComponent).registerLifecycleCallbacks()

    val config = FontRequestEmojiCompatConfig(
      this,
      FontRequest(
        "com.google.android.gms.fonts",
        "com.google.android.gms",
        "Noto Color Emoji Compat",
        R.array.com_google_android_gms_fonts_certs
      )
    )
    EmojiCompat.init(config)
  }
}