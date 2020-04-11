package com.illiarb.tmdbclient.initializers

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.core.FlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.soloader.SoLoader
import com.illiarb.tmdbclient.libs.tools.AppInitializer
import javax.inject.Inject

class FlipperInitializer @Inject constructor(
  private val plugins: Set<@JvmSuppressWildcards FlipperPlugin>,
  private val networkPlugin: NetworkFlipperPlugin
) : AppInitializer {

  override fun initialize(app: Application) {
    SoLoader.init(app, /* nativeExoPackage */ false)

    val client = AndroidFlipperClient.getInstance(app)

    plugins.forEach(client::addPlugin)

    client.addPlugin(networkPlugin)
    client.start()
  }
}