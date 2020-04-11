package com.illiarb.tmdbclient.tools

import android.app.Application

interface AppInitializer {

  fun initialize(app: Application)
}