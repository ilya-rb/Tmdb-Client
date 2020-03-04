package com.illiarb.tmdbcliient.coretest.navigation

import com.illiarb.tmdblcient.core.navigation.Router

class TestRouter : Router {
  override fun executeAction(action: Router.Action): Router.Action = action
}