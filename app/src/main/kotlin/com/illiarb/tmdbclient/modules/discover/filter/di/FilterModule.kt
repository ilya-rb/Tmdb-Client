package com.illiarb.tmdbclient.modules.discover.filter.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.libs.ui.di.ViewModelKey
import com.illiarb.tmdbclient.modules.discover.filter.FilterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface FilterModule {

  @Binds
  @IntoMap
  @ViewModelKey(FilterViewModel::class)
  fun bindFilterViewModel(viewModel: FilterViewModel): ViewModel
}