package com.illiarb.tmdbclient.modules.main

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import com.illiarb.tmdbclient.libs.buildconfig.BuildConfig
import com.illiarb.tmdbclient.libs.tools.ConnectivityStatus
import com.illiarb.tmdbclient.navigation.DeepLinkHandler
import com.illiarb.tmdbclient.navigation.NavigatorHolder
import com.illiarb.tmdbclient.system.DayNightModeChangeNotifier
import dagger.BindsInstance
import dagger.Component

/**
 * @author ilya-rb on 28.12.18.
 */
@Component(
  dependencies = [MainComponent.Dependencies::class],
  modules = [MainModule::class]
)
interface MainComponent {

  interface Dependencies {
    fun connectivityStatus(): ConnectivityStatus
    fun navigatorHolder(): NavigatorHolder
    fun buildConfig(): BuildConfig
    fun fragmentFactory(): FragmentFactory
    fun deepLinkHandler(): DeepLinkHandler
    fun systemChangesNotifier(): DayNightModeChangeNotifier
  }

  @Component.Factory
  interface Factory {

    fun create(
      @BindsInstance activity: FragmentActivity,
      dependencies: Dependencies
    ): MainComponent
  }

  fun inject(activity: MainActivity)
}