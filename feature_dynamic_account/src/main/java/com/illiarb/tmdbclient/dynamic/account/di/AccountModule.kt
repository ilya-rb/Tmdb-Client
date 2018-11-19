package com.illiarb.tmdbclient.dynamic.account.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.dynamic.account.AccountViewModel
import com.illiarb.tmdbexplorer.coreui.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author ilya-rb on 19.11.18.
 */
@Module
interface AccountModule {

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    fun bindAccountViewModel(impl: AccountViewModel): ViewModel
}