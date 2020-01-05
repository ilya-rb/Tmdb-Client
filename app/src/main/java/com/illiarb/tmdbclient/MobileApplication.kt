package com.illiarb.tmdbclient

import android.app.Application
import com.google.firebase.FirebaseApp
import com.illiarb.tmdbclient.di.AppComponent
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.storage.WorkManager
import com.illiarb.tmdblcient.core.storage.WorkManager.WorkType
import com.illiarb.tmdblcient.core.tools.Logger
import timber.log.Timber
import javax.inject.Inject

class MobileApplication : Application(), App {

    @Inject
    lateinit var workManager: WorkManager

    private val applicationProvider by lazy { AppComponent.get(this) }

    override fun onCreate() {
        super.onCreate()

        val appComponent = applicationProvider as AppComponent
        appComponent.inject(this)

        configureDi()
        configureLogger()
        configureWorkers()

        FirebaseApp.initializeApp(this)
    }

    override fun getApplication(): Application = this

    override fun getAppProvider(): AppProvider = applicationProvider

    private fun configureDi() = MobileAppInjector(this).registerLifecycleCallbacks()

    private fun configureWorkers() {
        workManager.scheduleWork(WorkType.ConfigurationFetch)
    }

    private fun configureLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Logger.addLoggingStrategy(object : Logger.LoggingStrategy {
            override fun log(priority: Logger.Priority, message: String, throwable: Throwable?) {
                when (priority) {
                    Logger.Priority.WARN -> Timber.w(message)
                    Logger.Priority.DEBUG -> Timber.d(message)
                    Logger.Priority.INFO -> Timber.i(message)
                    Logger.Priority.ERROR -> Timber.e(throwable)
                }
            }
        })
    }
}