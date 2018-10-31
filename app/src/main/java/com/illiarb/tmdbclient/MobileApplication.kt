package com.illiarb.tmdbclient

import android.app.Application
import android.os.Looper
import com.illiarb.tmdbclient.di.AppComponent
import com.illiarb.tmdbexplorerdi.App
import com.illiarb.tmdbexplorerdi.AppInjector
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers

@Suppress("unused")
class MobileApplication : Application(), App {

    private val applicationProvider by lazy { AppComponent.get(this) }

    override fun onCreate() {
        super.onCreate()

        configureRxJava()
        configureDi()
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
}