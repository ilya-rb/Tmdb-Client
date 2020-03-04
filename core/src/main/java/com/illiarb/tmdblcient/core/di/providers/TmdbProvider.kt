package com.illiarb.tmdblcient.core.di.providers

import com.illiarb.tmdblcient.core.tools.WorkerCreator
import com.illiarb.tmdblcient.core.util.DateFormatter

interface TmdbProvider {

  fun provideConfigurationWorkCreator(): WorkerCreator

  fun provideDateFormatter(): DateFormatter

}