package com.illiarb.tmdbclient.ui.main

import androidx.fragment.app.FragmentActivity
import com.illiarb.tmdbclient.navigation.NavigatorHolder
import com.illiarb.tmdbclient.tools.ConnectivityStatus
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
  }

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun activity(activity: FragmentActivity): Builder
    fun dependencies(dependencies: Dependencies): Builder
    fun build(): MainComponent
  }

  fun inject(activity: MainActivity)
}