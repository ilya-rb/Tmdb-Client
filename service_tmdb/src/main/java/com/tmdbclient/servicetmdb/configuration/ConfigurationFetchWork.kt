package com.tmdbclient.servicetmdb.configuration

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tmdbclient.servicetmdb.repository.ConfigurationRepository
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * @author ilya-rb on 03.12.18.
 */
class ConfigurationFetchWork(
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
          is com.illiarb.tmdblcient.core.util.Result.Ok -> Result.success()
          is com.illiarb.tmdblcient.core.util.Result.Err -> Result.failure()
        }
      }
      Result.success()
    } catch (e: IOException) {
      Result.retry()
    }
  }
}