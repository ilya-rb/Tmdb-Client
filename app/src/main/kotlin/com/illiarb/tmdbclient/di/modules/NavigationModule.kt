package com.illiarb.tmdbclient.di.modules

import androidx.fragment.app.Fragment
import com.illiarb.tmdbclient.modules.video.VideoListFragment
import com.illiarb.tmdbclient.navigation.NavigatorHolder
import com.illiarb.tmdbclient.navigation.Router
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

/**
 * @author ilya-rb on 31.01.19.
 */
@Module
interface NavigationModule {

  @Module
  companion object {

    @Provides
    @JvmStatic
    @DefaultVideosFragmentClassName
    fun provideDefaultVideosFragmentClassName(): Class<out Fragment> = VideoListFragment::class.java
  }

  @Binds
  fun bindRouter(router: Router.DefaultRouter): Router

  @Binds
  fun bindNavigatorHolder(holder: NavigatorHolder.ActionsBuffer): NavigatorHolder

  @Qualifier
  @Retention(AnnotationRetention.RUNTIME)
  annotation class DefaultVideosFragmentClassName
}