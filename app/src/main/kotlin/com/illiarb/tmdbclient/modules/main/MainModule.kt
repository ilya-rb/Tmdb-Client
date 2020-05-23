package com.illiarb.tmdbclient.modules.main

import com.illiarb.tmdbclient.navigation.Navigator
import dagger.Binds
import dagger.Module

@Module
interface MainModule {

  @Binds
  fun bindNavigator(navigator: Navigator.DefaultNavigator): Navigator
}