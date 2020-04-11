package com.illiarb.tmdbclient.navigation

interface Navigator {

  fun executeAction(action: Router.Action)
}