package com.illiarb.tmdbclient.initializers

import android.app.Application
import com.illiarb.tmdbclient.BuildConfig
import com.illiarb.tmdbclient.libs.logger.Logger
import com.illiarb.tmdbclient.libs.logger.LoggerPriority
import com.illiarb.tmdbclient.libs.logger.LoggingStrategy
import com.illiarb.tmdbclient.libs.tools.AppInitializer
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
          LoggerPriority.Warn -> Timber.w(throwable, message)
          LoggerPriority.Debug -> Timber.d(throwable, message)
          LoggerPriority.Info -> Timber.i(throwable, message)
          LoggerPriority.Error -> Timber.e(throwable, message)
        }
      }
    })
  }
}