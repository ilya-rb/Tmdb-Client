package com.illiarb.tmdbclient.di.modules

import android.app.Application
import com.facebook.flipper.core.FlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.illiarb.tmdbclient.AppBuildConfig
import com.illiarb.tmdbclient.AppTmdbConfig
import com.illiarb.tmdbclient.libs.buildconfig.BuildConfig
import com.illiarb.tmdbclient.libs.buildconfig.TmdbConfig
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
  @JvmStatic
  fun provideBuildConfig(app: Application): BuildConfig = AppBuildConfig(app)

  @Provides
  @JvmStatic
  fun provideTmdbConfig(): TmdbConfig = AppTmdbConfig()
}