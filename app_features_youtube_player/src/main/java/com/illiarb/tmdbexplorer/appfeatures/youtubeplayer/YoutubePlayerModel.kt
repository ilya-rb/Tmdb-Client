package com.illiarb.tmdbexplorer.appfeatures.youtubeplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbexplorer.appfeatures.youtubeplayer.YoutubePlayerModel.UiEvent
import com.illiarb.tmdbexplorer.appfeatures.youtubeplayer.YoutubePlayerModel.UiVideo
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.domain.Video
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import com.illiarb.tmdblcient.core.util.Result
import kotlinx.coroutines.launch

interface YoutubePlayerModel {

    val videos: LiveData<List<UiVideo>>

    val selectedVideo: LiveData<UiVideo>

    fun onUiEvent(event: UiEvent)

    data class UiVideo(val video: Video, val isSelected: Boolean)

    sealed class UiEvent {
        class ItemClick(val item: Any) : UiEvent()
    }
}

class DefaultYoutubePlayerModel(
    private val movieId: Int,
    private val moviesInteractor: MoviesInteractor
) : BasePresentationModel(), YoutubePlayerModel {

    private val _videos = MutableLiveData<List<UiVideo>>()

    private val _selectedVideo = _videos.map { videos ->
        videos.first { it.isSelected }
    }

    init {
        viewModelScope.launch {
            _videos.value = when (val result = moviesInteractor.getMovieVideos(movieId)) {
                is Result.Success -> result.data.mapIndexed { index, video ->
                    if (index == 0) {
                        UiVideo(video, isSelected = true)
                    } else {
                        UiVideo(video, isSelected = false)
                    }
                }
                is Result.Error -> emptyList()
            }
        }
    }

    override val videos: LiveData<List<UiVideo>>
        get() = _videos

    override val selectedVideo: LiveData<UiVideo>
        get() = _selectedVideo

    override fun onUiEvent(event: UiEvent) {
        if (event is UiEvent.ItemClick) {
            val item = event.item as UiVideo

            _videos.value?.let {
                val position = it.indexOf(item)
                if (position != -1) {
                    _videos.value = it.mapIndexed { index, uiVideo ->
                        if (index == position) {
                            uiVideo.copy(isSelected = true)
                        } else {
                            uiVideo.copy(isSelected = false)
                        }
                    }
                }
            }
        }
    }
}