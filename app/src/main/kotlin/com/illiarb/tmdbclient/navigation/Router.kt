package com.illiarb.tmdbclient.navigation

import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author ilya-rb on 18.11.18.
 */
interface Router {

  fun executeAction(action: Action): Action

  @Singleton
  class DefaultRouter @Inject constructor(private val navigatorHolder: NavigatorHolder) : Router {

    override fun executeAction(action: Action): Action {
      navigatorHolder.executeAction(action)
      return action
    }
  }
}