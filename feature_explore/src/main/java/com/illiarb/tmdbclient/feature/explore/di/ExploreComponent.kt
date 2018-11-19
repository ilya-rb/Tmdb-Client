package com.illiarb.tmdbclient.feature.explore.di

import androidx.fragment.app.FragmentActivity
import com.illiarb.tmdbclient.feature.explore.ExploreFragment
import com.illiarb.tmdbexplorer.coreui.di.ActivityModule
import com.illiarb.tmdbexplorer.coreui.di.UiEventsModule
import com.illiarb.tmdbexplorer.coreui.di.ViewModelModule
import com.illiarb.tmdbexplorer.coreui.di.scope.FragmentScope
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import dagger.Component

/**
 * @author ilya-rb on 31.10.18.
 */
@Component(
    dependencies = [AppProvider::class],
    modules = [
        ExploreModule::class,
        ViewModelModule::class,
        ActivityModule::class,
        UiEventsModule::class
    ]
)
@FragmentScope
interface ExploreComponent {

    companion object {

        fun get(appProvider: AppProvider, activity: FragmentActivity): ExploreComponent =
            DaggerExploreComponent.builder()
                .appProvider(appProvider)
                .activityModule(ActivityModule(activity))
                .build()
    }

    fun inject(fragment: ExploreFragment)
}