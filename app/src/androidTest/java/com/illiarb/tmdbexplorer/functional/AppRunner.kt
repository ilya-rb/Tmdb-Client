package com.illiarb.tmdbexplorer.functional

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class AppRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, TestApplication::class.java.name, context)
    }
}