package com.illiarb.tmdbclient.di.modules

import android.app.Application
import com.facebook.flipper.core.FlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
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
}