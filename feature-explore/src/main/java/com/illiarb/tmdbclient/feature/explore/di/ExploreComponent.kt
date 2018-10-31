package com.illiarb.tmdbclient.feature.explore.di

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
        ViewModelModule::class
    ]
)
interface ExploreComponent {

    companion object {

        fun get(appProvider: AppProvider): ExploreComponent =
            DaggerExploreComponent.builder()
                .appProvider(appProvider)
                .build()
    }

    fun inject(fragment: ExploreFragment)
}