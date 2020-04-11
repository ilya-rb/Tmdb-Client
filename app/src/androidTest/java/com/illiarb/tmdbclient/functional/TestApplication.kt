package com.illiarb.tmdbclient.functional

import androidx.core.provider.FontRequest
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig
import com.illiarb.tmdbclient.MobileApplication
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.di.AppComponent
import com.illiarb.tmdbclient.di.AppInjector

// TODO: Fix this
class TestApplication : MobileApplication() {

  private val testAppComponent = TestAppComponent()

  override val appComponent: AppComponent get() = testAppComponent

  override fun onCreate() {
    super.onCreate()
    AppInjector(this).registerLifecycleCallbacks()

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