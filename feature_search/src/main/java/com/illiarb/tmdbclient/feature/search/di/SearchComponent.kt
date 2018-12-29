package com.illiarb.tmdbclient.feature.search.di

import com.illiarb.tmdbclient.feature.search.ui.SearchFragment
import com.illiarb.tmdbexplorer.coreui.di.UiEventsModule
import com.illiarb.tmdbexplorer.coreui.di.scope.FragmentScope
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import dagger.Component

/**
 * @author ilya-rb on 26.12.18.
 */
@Component(
    dependencies = [AppProvider::class],
    modules = [UiEventsModule::class]
)
@FragmentScope
interface SearchComponent {

    companion object {

        fun get(appProvider: AppProvider): SearchComponent =
            DaggerSearchComponent.builder()
                .appProvider(appProvider)
                .build()
    }

    fun inject(fragment: SearchFragment)
}