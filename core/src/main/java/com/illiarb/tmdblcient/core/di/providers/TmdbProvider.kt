package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.tools.WorkerCreator

interface TmdbProvider {

    fun provideConfigurationWorkCreator(): WorkerCreator

}