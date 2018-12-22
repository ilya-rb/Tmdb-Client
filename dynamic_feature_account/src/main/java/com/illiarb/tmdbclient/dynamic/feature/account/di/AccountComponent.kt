package com.illiarb.tmdbclient.dynamic.feature.account.di

import com.illiarb.tmdbclient.dynamic.feature.account.AccountFragment
import com.illiarb.tmdbclient.dynamic.feature.account.auth.ui.AuthFragment
import com.illiarb.tmdbexplorer.coreui.di.UiEventsModule
import com.illiarb.tmdbexplorer.coreui.di.ViewModelModule
import com.illiarb.tmdbexplorer.coreui.di.scope.FragmentScope
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import dagger.Component

/**
 * @author ilya-rb on 20.11.18.
 */
@Component(
    dependencies = [AppProvider::class],
    modules = [
        AccountModule::class,
        ViewModelModule::class,
        UiEventsModule::class
    ]
)
@FragmentScope
interface AccountComponent {

    companion object {

        fun get(appProvider: AppProvider): AccountComponent =
            DaggerAccountComponent.builder()
                .appProvider(appProvider)
                .build()
    }

    fun inject(fragment: AccountFragment)

    fun inject(fragment: AuthFragment)
}