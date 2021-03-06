package com.illiarb.tmdbclient.modules.discover.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.modules.discover.DiscoverViewModel
import com.illiarb.tmdbclient.libs.ui.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface DiscoverModule {

  @Binds
  @IntoMap
  @ViewModelKey(DiscoverViewModel::class)
  fun bindDiscoverViewModel(viewModel: DiscoverViewModel): ViewModel
}