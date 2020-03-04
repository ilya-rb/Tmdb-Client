package com.illiarb.tmdbclient.tools.di.modules

import com.illiarb.tmdbclient.tools.AndroidConnectivityStatus
import com.illiarb.tmdblcient.core.app.App
import com.illiarb.tmdblcient.core.tools.ConnectivityStatus
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author ilya-rb on 31.01.19.
 */
@Module
class AndroidModule(private val app: App) {

  @Provides
  @Singleton
  fun provideConnectivityStatus(): ConnectivityStatus = AndroidConnectivityStatus(app)
}