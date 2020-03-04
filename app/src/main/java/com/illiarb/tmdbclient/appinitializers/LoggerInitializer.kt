package com.illiarb.tmdbclient.appinitializers

import com.illiarb.tmdbclient.BuildConfig
import com.illiarb.tmdblcient.core.app.AppInitializer
import com.illiarb.tmdblcient.core.app.App
import com.illiarb.tmdblcient.core.tools.Logger
import timber.log.Timber
import javax.inject.Inject

class LoggerInitializer @Inject constructor() : AppInitializer {

  override fun initialize(app: App) {
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }

    Logger.addLoggingStrategy(object : Logger.LoggingStrategy {
      override fun log(tag: String, priority: Logger.Priority, message: String, throwable: Throwable?) {
        when (priority) {
          Logger.Priority.WARN -> Timber.tag(tag).w(throwable, message)
          Logger.Priority.DEBUG -> Timber.tag(tag).d(throwable, message)
          Logger.Priority.INFO -> Timber.tag(tag).i(throwable, message)
          Logger.Priority.ERROR -> Timber.tag(tag).e(throwable, message)
        }
      }
    })
  }
}