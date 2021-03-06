package com.illiarb.tmdbclient.di.modules

import android.app.Application
import com.illiarb.tmdbclient.AppBuildConfig
import com.illiarb.tmdbclient.AppTmdbConfig
import com.illiarb.tmdbclient.libs.buildconfig.BuildConfig
import com.illiarb.tmdbclient.libs.buildconfig.TmdbConfig
import com.illiarb.tmdbclient.system.DayNightModeChangeNotifier
import com.illiarb.tmdbclient.system.DayNightModePreferences
import com.illiarb.tmdbclient.system.InMemoryDayNightModePreferences
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * @author ilya-rb on 03.11.18.
 */
@Module
interface AppModule {

  @Binds
  fun bindDayNightModePreferences(
    preferences: InMemoryDayNightModePreferences
  ): DayNightModePreferences

  @Binds
  fun bindDayNightChangeNotifier(
    notifier: InMemoryDayNightModePreferences
  ): DayNightModeChangeNotifier

  @Module
  companion object {

    @Provides
    @JvmStatic
    fun provideBuildConfig(app: Application): BuildConfig = AppBuildConfig(app)

    @Provides
    @JvmStatic
    fun provideTmdbConfig(): TmdbConfig = AppTmdbConfig()
  }
}