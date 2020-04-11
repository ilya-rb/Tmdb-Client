package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdbclient.appinitializers.EmojiInitializer
import com.illiarb.tmdbclient.appinitializers.LoggerInitializer
import com.illiarb.tmdbclient.appinitializers.WorkManagerInitializer
import com.illiarb.tmdbclient.tools.AppInitializer
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
  fun provideAppInitializers(
    workManagerInitializer: WorkManagerInitializer,
    loggerInitializer: LoggerInitializer,
    emojiInitializer: EmojiInitializer
  ): Set<AppInitializer> {
    return setOf(loggerInitializer, workManagerInitializer, emojiInitializer)
  }
}