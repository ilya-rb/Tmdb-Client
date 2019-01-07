package com.illiarb.tmdbexplorer.coreui.di.modules

import com.illiarb.tmdbexplorer.coreui.di.scope.FragmentScope
import com.illiarb.tmdbexplorer.coreui.pipeline.UiPipelineData
import com.illiarb.tmdblcient.core.pipeline.EventPipeline
import com.illiarb.tmdblcient.core.pipeline.SimpleEventPipeline
import dagger.Module
import dagger.Provides

/**
 * @author ilya-rb on 18.11.18.
 */
@Module
class UiEventsModule {

    @Provides
    @FragmentScope
    fun provideUiEventsPipeline(): EventPipeline<UiPipelineData> = SimpleEventPipeline()
}