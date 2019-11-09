package com.illiarb.tmdbclient.di

import androidx.fragment.app.FragmentActivity
import com.illiarb.tmdbclient.MainActivity
import com.illiarb.tmdbclient.di.modules.MainModule
import com.illiarb.tmdbexplorer.coreui.di.ActivityModule
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import dagger.Component

/**
 * @author ilya-rb on 28.12.18.
 */
@Component(
    dependencies = [AppProvider::class],
    modules = [ActivityModule::class, MainModule::class]
)
interface MainComponent {

    companion object {
        fun get(appProvider: AppProvider, activity: FragmentActivity): MainComponent =
            DaggerMainComponent.builder()
                .appProvider(appProvider)
                .activityModule(ActivityModule(activity))
                .build()
    }

    fun inject(activity: MainActivity)
}