package com.illiarb.tmdbclient.navigation

import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author ilya-rb on 31.01.19.
 */
@Singleton
class AppRouter @Inject constructor(private val navigatorHolder: NavigatorHolder) : Router {

  override fun executeAction(action: Router.Action): Router.Action {
    navigatorHolder.executeAction(action)
    return action
  }
}