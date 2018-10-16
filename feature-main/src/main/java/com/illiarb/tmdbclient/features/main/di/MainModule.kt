package com.illiarb.tmdbclient.features.main.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.features.main.MainViewModel
import com.illiarb.tmdbexplorer.coreui.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}