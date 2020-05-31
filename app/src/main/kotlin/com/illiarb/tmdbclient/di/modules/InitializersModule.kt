package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdbclient.initializers.EmojiInitializer
import com.illiarb.tmdbclient.initializers.FlipperInitializer
import com.illiarb.tmdbclient.initializers.LoggerInitializer
import com.illiarb.tmdbclient.libs.tools.AppInitializer
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
interface InitializersModule {

  @Binds
  @IntoSet
  fun bindFlipperInitializer(initializer: FlipperInitializer): AppInitializer

  @Binds
  @IntoSet
  fun bindLoggerInitializer(initializer: LoggerInitializer): AppInitializer

  @Binds
  @IntoSet
  fun bindEmojiInitializer(initializer: EmojiInitializer): AppInitializer
}