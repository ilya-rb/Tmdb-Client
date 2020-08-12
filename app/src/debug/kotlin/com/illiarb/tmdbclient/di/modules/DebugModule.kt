
package com.illiarb.tmdbclient.di.modules

import android.app.Application
import com.facebook.flipper.core.FlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.illiarb.tmdbclient.initializers.FlipperInitializer
import com.illiarb.tmdbclient.initializers.StrictModeInitializer
import com.illiarb.tmdbclient.libs.tools.AppInitializer
import com.illiarb.tmdbclient.services.tmdb.di.NetworkInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import dagger.multibindings.IntoSet
import dagger.multibindings.Multibinds
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
interface DebugModule {

  @Multibinds
  fun bindFlipperPlugins(): Set<FlipperPlugin>

  @Binds
  @IntoSet
  fun bindNetworkFlipperPluginIntoSet(plugin: NetworkFlipperPlugin): FlipperPlugin

  @Binds
  @IntoSet
  fun bindFlipperInitializer(initializer: FlipperInitializer): AppInitializer

  @Binds
  @IntoSet
  fun bindStrictModeInitializer(initializer: StrictModeInitializer): AppInitializer

  @Module
  companion object {

    @Provides
    @ElementsIntoSet
    @JvmStatic
    fun provideFlipperPlugins(app: Application): Set<FlipperPlugin> {
      return setOf(InspectorFlipperPlugin(app, DescriptorMapping.withDefaults()))
    }

    @Provides
    @JvmStatic
    @Singleton
    fun provideNetworkFlipperPlugin() = NetworkFlipperPlugin()

    @Provides
    @JvmStatic
    @IntoSet
    @NetworkInterceptor
    fun provideFlipperInterceptor(plugin: NetworkFlipperPlugin): Interceptor {
      return FlipperOkhttpInterceptor(plugin)
    }
  }
}