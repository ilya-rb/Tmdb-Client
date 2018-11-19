package com.illiarb.tmdbclient.features.main.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.features.main.MainViewModel
import com.illiarb.tmdbclient.features.main.navigation.MainNavigator
import com.illiarb.tmdbexplorer.coreui.di.ViewModelKey
import com.illiarb.tmdblcient.core.navigation.Navigator
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author ilya-rb on 31.10.18.
 */
@Module
interface MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    fun bindNavigator(impl: MainNavigator): Navigator
}