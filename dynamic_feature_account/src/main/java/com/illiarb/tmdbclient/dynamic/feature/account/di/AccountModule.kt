package com.illiarb.tmdbclient.dynamic.feature.account.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.dynamic.feature.account.auth.presentation.AuthModel
import com.illiarb.tmdbclient.dynamic.feature.account.profile.presentation.AccountModel
import com.illiarb.tmdbexplorer.coreui.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author ilya-rb on 08.01.19.
 */
@Module
interface AccountModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthModel::class)
    fun bindAuthModel(impl: AuthModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountModel::class)
    fun bindAccountModel(impl: AccountModel): ViewModel
}