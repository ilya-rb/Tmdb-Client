package com.illiarb.tmdbclient.libs.ui.main

import com.illiarb.tmdbclient.navigation.AppNavigator
import com.illiarb.tmdbclient.navigation.Navigator
import dagger.Binds
import dagger.Module

@Module
interface MainModule {

  @Binds
  fun bindNavigator(navigator: AppNavigator): Navigator
}