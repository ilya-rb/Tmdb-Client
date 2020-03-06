package com.illiarb.tmdbclient

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.provider.FontRequest
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig
import com.google.firebase.FirebaseApp
import com.illiarb.tmdbclient.di.AppComponent
import com.illiarb.tmdblcient.core.app.App
import com.illiarb.tmdblcient.core.app.AppInitializer
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import javax.inject.Inject

class MobileApplication : Application(), App {

  @Inject
  lateinit var appInitializers: Set<@JvmSuppressWildcards AppInitializer>

  private val applicationProvider by lazy {
    AppComponent.get(this)
  }

  override fun onCreate() {
    super.onCreate()

    val appComponent = applicationProvider as AppComponent
    appComponent.inject(this)

    MobileAppInjector(this).registerLifecycleCallbacks()

    FirebaseApp.initializeApp(this)

    appInitializers.forEach {
      it.initialize(this)
    }

    initEmojiFont()
  }

  override fun getApplication(): Application = this

  override fun getAppProvider(): AppProvider = applicationProvider

  private fun initEmojiFont() {
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