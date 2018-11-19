package com.illiarb.tmdbclient.dynamic.account.di

import com.illiarb.tmdbclient.dynamic.account.AccountFragment
import com.illiarb.tmdbexplorer.coreui.di.scope.FragmentScope
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import dagger.Component

/**
 * @author ilya-rb on 19.11.18.
 */
@Component(dependencies = [AppProvider::class], modules = [AccountModule::class])
@FragmentScope
interface AccountComponent {

    companion object {
        fun get(appProvider: AppProvider): AccountComponent =
            DaggerAccountComponent.builder()
                .appProvider(appProvider)
                .build()
    }

    fun inject(fragment: AccountFragment)
}