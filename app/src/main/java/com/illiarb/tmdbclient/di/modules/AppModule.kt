package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdbclient.appinitializers.EmojiInitializer
import com.illiarb.tmdbclient.appinitializers.LoggerInitializer
import com.illiarb.tmdbclient.appinitializers.WorkManagerInitializer
import com.illiarb.tmdblcient.core.app.AppInitializer
import com.illiarb.tmdblcient.core.app.App
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet

/**
 * @author ilya-rb on 03.11.18.
 */
@Module
class AppModule(val app: App) {

  @Provides
  fun provideApp(): App = app

  @Provides
  @ElementsIntoSet
  fun provideAppInitializers(
    workManagerInitializer: WorkManagerInitializer,
    loggerInitializer: LoggerInitializer,
    emojiInitializer: EmojiInitializer
  ): Set<AppInitializer> {
    return setOf(loggerInitializer, workManagerInitializer, emojiInitializer)
  }
}