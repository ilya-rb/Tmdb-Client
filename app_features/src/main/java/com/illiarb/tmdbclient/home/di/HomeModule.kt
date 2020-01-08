package com.illiarb.tmdbclient.home.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.home.DefaultHomeModel
import com.illiarb.tmdbexplorer.coreui.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface HomeModule {

    @Binds
    @IntoMap
    @ViewModelKey(DefaultHomeModel::class)
    fun bindHomeModel(impl: DefaultHomeModel): ViewModel
}