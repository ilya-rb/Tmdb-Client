package com.illiarb.tmdbclient.di.modules

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.illiarb.tmdbclient.libs.ui.di.DaggerFragmentFactory
import com.illiarb.tmdbclient.libs.ui.di.FragmentKey
import com.illiarb.tmdbclient.modules.home.HomeFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface FragmentsModule {

  @Binds
  @IntoMap
  @FragmentKey(HomeFragment::class)
  fun bindHomeFragment(fragment: HomeFragment): Fragment

  @Binds
  fun bindFragmentFactory(factory: DaggerFragmentFactory): FragmentFactory
}