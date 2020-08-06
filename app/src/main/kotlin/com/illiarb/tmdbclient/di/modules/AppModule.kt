package com.illiarb.tmdbclient.di.modules

import android.app.Application
import com.illiarb.tmdbclient.AppBuildConfig
import com.illiarb.tmdbclient.AppTmdbConfig
import com.illiarb.tmdbclient.libs.buildconfig.BuildConfig
import com.illiarb.tmdbclient.libs.buildconfig.TmdbConfig
import dagger.Module
import dagger.Provides

/**
 * @author ilya-rb on 03.11.18.
 */
@Module
object AppModule {

  @Provides
  @JvmStatic
  fun provideBuildConfig(app: Application): BuildConfig = AppBuildConfig(app)

  @Provides
  @JvmStatic
  fun provideTmdbConfig(): TmdbConfig = AppTmdbConfig()
}