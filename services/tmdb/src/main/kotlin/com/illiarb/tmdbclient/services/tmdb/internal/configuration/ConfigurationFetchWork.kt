package com.illiarb.tmdbclient.services.tmdb.internal.configuration

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.illiarb.tmdbclient.services.tmdb.internal.repository.ConfigurationRepository
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.util.concurrent.TimeUnit
import com.illiarb.tmdbclient.libs.util.Result as UtilResult

/**
 * @author ilya-rb on 03.12.18.
 */
internal class ConfigurationFetchWork(
  context: Context,
  workerParameters: WorkerParameters,
  private val configurationRepository: ConfigurationRepository
) : Worker(context, workerParameters) {

  companion object {

    // 2 Days
    private const val REPEAT_INTERVAL = 2L

    fun createWorkRequest(): PeriodicWorkRequest =
      PeriodicWorkRequestBuilder<ConfigurationFetchWork>(REPEAT_INTERVAL, TimeUnit.DAYS)
        .addTag(ConfigurationFetchWork::class.java.name)
        .setConstraints(
          Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        )
        .build()
  }

  override fun doWork(): Result {
    return try {
      runBlocking {
        when (configurationRepository.getConfiguration(refresh = true)) {
          is UtilResult.Ok -> Result.success()
          is UtilResult.Err -> Result.failure()
        }
      }
      Result.success()
    } catch (e: IOException) {
      Result.retry()
    }
  }
}