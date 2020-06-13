package com.illiarb.tmdbclient.initializers

import android.app.Application
import com.google.firebase.FirebaseApp
import com.illiarb.tmdbclient.libs.tools.AppInitializer
import javax.inject.Inject

class FirebaseInitializer @Inject constructor() : AppInitializer {

  override fun initialize(app: Application) {
    FirebaseApp.initializeApp(app)
  }
}