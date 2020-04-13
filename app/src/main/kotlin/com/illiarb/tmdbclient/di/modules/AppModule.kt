package com.illiarb.tmdbclient.di.modules

import android.app.Application
import com.facebook.flipper.core.FlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.illiarb.tmdbclient.initializers.EmojiInitializer
import com.illiarb.tmdbclient.initializers.FlipperInitializer
import com.illiarb.tmdbclient.initializers.LoggerInitializer
import com.illiarb.tmdbclient.initializers.WorkManagerInitializer
import com.illiarb.tmdbclient.libs.tools.AppInitializer
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet

/**
 * @author ilya-rb on 03.11.18.
 */
@Module
object AppModule {

  @Provides
  @ElementsIntoSet
  @JvmStatic
  fun provideFlipperPlugins(app: Application): Set<FlipperPlugin> {
    return setOf(InspectorFlipperPlugin(app, DescriptorMapping.withDefaults()))
  }

  @Provides
  @ElementsIntoSet
  @JvmStatic
  fun provideAppInitializers(
    workManagerInitializer: WorkManagerInitializer,
    loggerInitializer: LoggerInitializer,
    emojiInitializer: EmojiInitializer,
    flipperInitializer: FlipperInitializer
  ): Set<AppInitializer> {
    return setOf(loggerInitializer, workManagerInitializer, emojiInitializer, flipperInitializer)
  }
}