package com.illiarb.tmdbclient.services.tmdb.di

import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object FlipperModule {

  @Provides
  @JvmStatic
  @Singleton
  fun provideNetworkFlipperPlugin() = NetworkFlipperPlugin()

  @Provides
  @JvmStatic
  @Singleton
  fun provideFlipperInterceptor(plugin: NetworkFlipperPlugin): FlipperOkhttpInterceptor {
    return FlipperOkhttpInterceptor(plugin)
  }
}