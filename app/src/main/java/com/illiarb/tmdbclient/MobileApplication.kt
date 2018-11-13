package com.illiarb.tmdbclient

import android.app.Application
import android.os.Looper
import com.illiarb.tmdbclient.di.AppComponent
import com.illiarb.tmdbexplorerdi.App
import com.illiarb.tmdbexplorerdi.AppInjector
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdblcient.core.system.Logger
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber

@Suppress("unused")
class MobileApplication : Application(), App {

    private val applicationProvider by lazy { AppComponent.get(this) }

    override fun onCreate() {
        super.onCreate()

        configureRxJava()
        configureDi()
        configureLogger()
    }

    override fun getApplication(): Application = this

    override fun getAppProvider(): AppProvider = applicationProvider

    private fun configureDi() {
        AppInjector(this).registerLifecycleCallbacks()
    }

    private fun configureRxJava() {
        val asyncMainThreadScheduler = AndroidSchedulers.from(Looper.getMainLooper(), true)

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { asyncMainThreadScheduler }
        RxAndroidPlugins.setMainThreadSchedulerHandler { asyncMainThreadScheduler }
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