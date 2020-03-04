package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdbclient.navigation.AppNavigator
import com.illiarb.tmdblcient.core.navigation.Navigator
import dagger.Binds
import dagger.Module

@Module
interface MainModule {

  @Binds
  fun bindNavigator(navigator: AppNavigator): Navigator
}