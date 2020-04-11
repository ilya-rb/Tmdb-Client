package com.illiarb.tmdbclient.navigation

interface NavigatorHolder {

  fun executeAction(action: Router.Action)

  fun setNavigator(navigator: Navigator)

  fun removeNavigator()
}