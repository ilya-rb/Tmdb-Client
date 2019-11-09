package com.illiarb.tmdbclient.home

import androidx.lifecycle.LiveData
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.util.Async

interface HomeViewModel {

    val movieSections: LiveData<Async<List<MovieSection>>>

    val isAccountVisible: LiveData<Boolean>

    fun onUiEvent(event: HomeUiEvent)

    sealed class HomeUiEvent {
        data class ItemClick(val item: Any) : HomeUiEvent()
    }
}