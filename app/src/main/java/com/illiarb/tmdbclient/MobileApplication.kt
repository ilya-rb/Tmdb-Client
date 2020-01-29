package com.illiarb.tmdbclient

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.google.firebase.FirebaseApp
import com.illiarb.tmdbclient.di.AppComponent
import com.illiarb.tmdblcient.core.app.AppInitializer
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.tools.Logger
import timber.log.Timber
import javax.inject.Inject

class MobileApplication : Application(), App {

    @Inject
    lateinit var appInitializers: Set<@JvmSuppressWildcards AppInitializer>

    @Inject
    lateinit var workerFactory: WorkerFactory

    private val applicationProvider by lazy { AppComponent.get(this) }

    override fun onCreate() {
        super.onCreate()

        val appComponent = applicationProvider as AppComponent
        appComponent.inject(this)

        configureDi()
        configureLogger()
        configureWorkManager()

        FirebaseApp.initializeApp(this)

        appInitializers.forEach {
            it.initialize(this)
        }
    }

    override fun getApplication(): Application = this

    override fun getAppProvider(): AppProvider = applicationProvider

    private fun configureDi() = MobileAppInjector(this).registerLifecycleCallbacks()

    private fun configureWorkManager() {
        val config = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()

        WorkManager.initialize(this, config)
    }

    private fun configureLogger() {
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