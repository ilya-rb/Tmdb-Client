package com.illiarb.tmdblcient.core.app

import android.app.Application
import com.illiarb.tmdblcient.core.di.providers.AppProvider

/**
 * @author ilya-rb on 24.12.18.
 */
interface App {

    fun getApplication(): Application

    fun getAppProvider(): AppProvider
}