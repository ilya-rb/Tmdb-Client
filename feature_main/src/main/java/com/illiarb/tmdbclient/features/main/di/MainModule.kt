package com.illiarb.tmdbclient.features.main.di

import com.illiarb.tmdbclient.features.main.navigation.MainNavigator
import com.illiarb.tmdblcient.core.navigation.Navigator
import dagger.Binds
import dagger.Module

/**
 * @author ilya-rb on 31.10.18.
 */
@Module
interface MainModule {

    @Binds
    fun bindNavigator(impl: MainNavigator): Navigator
}