package com.illiarb.tmdbclient.feature.search.di

import com.illiarb.tmdbclient.feature.search.ui.SearchFragment
import com.illiarb.tmdbexplorer.coreui.di.ViewModelModule
import com.illiarb.tmdbexplorer.coreui.di.scope.FragmentScope
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * @author ilya-rb on 26.12.18.
 */
@ExperimentalCoroutinesApi
@Component(
    dependencies = [AppProvider::class],
    modules = [
        SearchModule::class,
        ViewModelModule::class
    ]
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