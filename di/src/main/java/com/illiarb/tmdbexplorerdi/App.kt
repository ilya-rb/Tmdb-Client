package com.illiarb.tmdbexplorerdi

import android.app.Application
import com.illiarb.tmdbexplorerdi.providers.AppProvider

interface App {

    fun getApplication(): Application

    fun getAppProvider(): AppProvider
}