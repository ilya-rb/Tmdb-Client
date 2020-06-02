package com.illiarb.tmdbclient.navigation

import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author ilya-rb on 18.11.18.
 */
interface Router {

  fun executeAction(action: NavigationAction): NavigationAction

  @Singleton
  class DefaultRouter @Inject constructor(private val navigatorHolder: NavigatorHolder) : Router {

    override fun executeAction(action: NavigationAction): NavigationAction {
      navigatorHolder.executeAction(action)
      return action
    }
  }
}