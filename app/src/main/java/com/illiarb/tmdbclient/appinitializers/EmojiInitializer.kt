package com.illiarb.tmdbclient.appinitializers

import androidx.core.provider.FontRequest
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig
import com.illiarb.tmdbclient.R
import com.illiarb.tmdblcient.core.app.App
import com.illiarb.tmdblcient.core.app.AppInitializer
import javax.inject.Inject

class EmojiInitializer @Inject constructor() : AppInitializer {

  override fun initialize(app: App) {
    val config = FontRequestEmojiCompatConfig(
      app.getApplication(),
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