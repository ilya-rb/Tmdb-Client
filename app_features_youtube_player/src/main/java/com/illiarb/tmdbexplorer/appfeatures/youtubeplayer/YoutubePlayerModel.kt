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
        object VideoEnded : UiEvent()
        class VideoClick(val video: UiVideo) : UiEvent()
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
        when (event) {
            is UiEvent.VideoClick -> selectVideo(event.video)
            is UiEvent.VideoEnded -> onVideoEnded()
        }
    }

    private fun onVideoEnded() {
        // Play next video in the list or if end is reached the first one
        _videos.value?.let { videos ->
            videos.indexOfFirst { it.isSelected }
                .let { if (it < videos.size - 1) it + 1 else 0 }
                .let { selectVideo(videos[it]) }
        }
    }

    private fun selectVideo(video: UiVideo) {
        _videos.value?.let {
            val position = it.indexOf(video).takeIf { pos -> pos != -1 } ?: return

            _videos.value = it.mapIndexed { index, item ->
                if (index == position) {
                    item.copy(isSelected = true)
                } else {
                    item.copy(isSelected = false)
                }
            }
        }
    }
}