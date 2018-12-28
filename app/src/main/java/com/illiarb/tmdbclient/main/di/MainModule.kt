package com.illiarb.tmdbclient.main.di

import com.illiarb.tmdbclient.main.navigation.MainNavigator
import com.illiarb.tmdblcient.core.navigation.Navigator
import dagger.Binds
import dagger.Module

/**
 * @author ilya-rb on 28.12.18.
 */
@Module
interface MainModule {

    @Binds
    fun bindNavigator(impl: MainNavigator): Navigator
}