package com.illiarb.tmdbclient.discover.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.discover.DefaultDiscoverModel
import com.illiarb.tmdbclient.discover.DiscoverModel
import com.illiarb.tmdbexplorer.coreui.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface DiscoverModule {

    @Binds
    @IntoMap
    @ViewModelKey(DefaultDiscoverModel::class)
    fun bindDiscoverModel(model: DefaultDiscoverModel): ViewModel
}