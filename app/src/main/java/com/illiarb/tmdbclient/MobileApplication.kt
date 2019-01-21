package com.illiarb.tmdbclient

import android.app.Application
import com.google.firebase.FirebaseApp
import com.illiarb.tmdbclient.di.AppComponent
import com.illiarb.tmdblcient.core.config.FeatureConfig
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.system.Logger
import com.illiarb.tmdblcient.core.system.WorkManager
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber
import javax.inject.Inject

class MobileApplication : Application(), App {

    @Inject
    lateinit var workManager: WorkManager

    @Inject
    lateinit var featureConfig: FeatureConfig

    private val applicationProvider by lazy { AppComponent.get(this) }

    override fun onCreate() {
        super.onCreate()

        configureDi()
        configureLogger()

        FirebaseApp.initializeApp(this)

        val appComponent = applicationProvider as AppComponent
        appComponent.inject(this)

        workManager.schedulerPeriodicConfigurationFetch()

        featureConfig.fetchConfiguration()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }

        LeakCanary.install(this)
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

        Logger.addLoggingStrategy { priority, message, throwable ->
            when (priority) {
                Logger.Priority.WARN -> Timber.w(message)
                Logger.Priority.DEBUG -> Timber.d(message)
                Logger.Priority.INFO -> Timber.i(message)
                Logger.Priority.ERROR -> Timber.e(throwable)
            }
        }
    }
}