package com.illiarb.tmdbclient.initializers

import android.app.Application
import androidx.core.provider.FontRequest
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.libs.tools.AppInitializer
import javax.inject.Inject

class EmojiInitializer @Inject constructor() : AppInitializer {

  override fun initialize(app: Application) {
    val config = FontRequestEmojiCompatConfig(
      app,
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