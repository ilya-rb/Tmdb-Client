package com.illiarb.tmdblcient.core.app

import com.illiarb.tmdblcient.core.di.App

interface AppInitializer {

    fun initialize(app: App)
}