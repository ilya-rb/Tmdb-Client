package com.illiarb.tmdbexplorer.functional

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

// Used in app/build.gradle
@Suppress("unused")
class AppRunner : AndroidJUnitRunner() {

  override fun newApplication(
    cl: ClassLoader?,
    className: String?,
    context: Context?
  ): Application {
    return super.newApplication(cl, TestApplication::class.java.name, context)
  }
}