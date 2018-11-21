package com.illiarb.tmdbclient.dynamic.feature.account.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.dynamic.feature.account.AccountViewModel
import com.illiarb.tmdbclient.dynamic.feature.account.auth.AuthViewModel
import com.illiarb.tmdbexplorer.coreui.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author ilya-rb on 20.11.18.
 */
@Module
interface AccountModule {

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    fun bindAccountViewModel(viewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    fun bindAuthViewModel(viewModel: AuthViewModel): ViewModel
}