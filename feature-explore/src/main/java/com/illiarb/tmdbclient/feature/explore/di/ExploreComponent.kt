package com.illiarb.tmdbclient.feature.explore.di

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.illiarb.tmdbclient.feature.explore.ExploreFragment
import com.illiarb.tmdbexplorer.coreui.di.ViewModelModule
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
        ActivityModule::class
    ]
)
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