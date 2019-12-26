package com.illiarb.tmdbclient.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.domain.Movie
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface DiscoverModel {

    val results: LiveData<List<Movie>>

    fun onUiEvent(event: UiEvent)

    sealed class UiEvent {
        object FabClick : UiEvent()
    }
}

class DefaultDiscoverModel @Inject constructor() : BasePresentationModel(), DiscoverModel {

    private val _results = flow<List<Movie>> {}.asLiveData()

    override val results: LiveData<List<Movie>>
        get() = _results

    override fun onUiEvent(event: DiscoverModel.UiEvent) {

    }
}