package com.illiarb.tmdbclient.libs.tools

import android.app.Application

interface AppInitializer {

  fun initialize(app: Application)
}