package com.illiarb.tmdbclient.appinitializers

import android.app.Application
import com.illiarb.tmdbclient.BuildConfig
import com.illiarb.tmdbclient.libs.tools.AppInitializer
import com.illiarb.tmdbclient.libs.logger.Logger
import com.illiarb.tmdbclient.libs.logger.LoggerPriority
import com.illiarb.tmdbclient.libs.logger.LoggingStrategy
import timber.log.Timber
import javax.inject.Inject

class LoggerInitializer @Inject constructor() : AppInitializer {

  override fun initialize(app: Application) {
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }

    Logger.addLoggingStrategy(object : LoggingStrategy {
      override fun log(
        tag: String,
        priority: LoggerPriority,
        message: String,
        throwable: Throwable?
      ) {
        when (priority) {
          LoggerPriority.Warn -> Timber.tag(tag).w(throwable, message)
          LoggerPriority.Debug -> Timber.tag(tag).d(throwable, message)
          LoggerPriority.Info -> Timber.tag(tag).i(throwable, message)
          LoggerPriority.Error -> Timber.tag(tag).e(throwable, message)
        }
      }
    })
  }
}