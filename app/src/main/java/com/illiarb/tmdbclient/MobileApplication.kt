package com.illiarb.tmdbclient

import android.app.Application
import com.illiarb.tmdbclient.di.AppComponent
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.system.Logger
import com.illiarb.tmdblcient.core.system.WorkManager
import timber.log.Timber
import javax.inject.Inject

class MobileApplication : Application(), App {

    @Inject
    lateinit var workManager: WorkManager

    private val applicationProvider by lazy { AppComponent.get(this) }

    override fun onCreate() {
        super.onCreate()

        configureDi()
        configureLogger()

        val appComponent = applicationProvider as AppComponent
        appComponent.inject(this)

        workManager.initialize()
        workManager.schedulerPeriodicConfigurationFetch()
    }

    override fun getApplication(): Application = this

    override fun getAppProvider(): AppProvider = applicationProvider

    private fun configureDi() {
        MobileAppInjector(this).registerLifecycleCallbacks()
    }

    private fun configureLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val timberPrinter = object : Logger.Printer {
            override fun log(priority: Logger.Priority, message: String, throwable: Throwable?) {
                when (priority) {
                    Logger.Priority.WARN -> Timber.w(message)
                    Logger.Priority.DEBUG -> Timber.d(message)
                    Logger.Priority.INFO -> Timber.i(message)
                    Logger.Priority.ERROR -> Timber.e(throwable, message)
                }
            }
        }

        Logger.addPrinter(timberPrinter)
    }
}