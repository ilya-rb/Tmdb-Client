package com.illiarb.tmdbclient.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.illiarb.tmdbclient.libs.ui.di.DaggerViewModelFactory
import com.illiarb.tmdbclient.libs.ui.di.ViewModelKey
import com.illiarb.tmdbclient.modules.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

  @Binds
  @IntoMap
  @ViewModelKey(HomeViewModel::class)
  fun bindHomeModel(impl: HomeViewModel): ViewModel

  @Binds
  fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}