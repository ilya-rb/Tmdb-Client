package com.illiarb.tmdbclient.video

import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbexplorer.coreui.base.viewmodel.BaseViewModel
import com.illiarb.tmdblcient.core.domain.Video
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import com.illiarb.tmdblcient.core.util.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoListViewModel @Inject constructor(
  private val movieId: Int,
  private val moviesInteractor: MoviesInteractor
) : BaseViewModel<VideoListViewModel.State, VideoListViewModel.Event>(initialState()) {

  companion object {
    private fun initialState() = State(videos = emptyList(), selected = null)
  }

  init {
    viewModelScope.launch {
      when (val result = moviesInteractor.getMovieVideos(movieId)) {
        is Result.Success -> {
          val groupedVideos = result.data
            .mapIndexed { index, video ->
              if (index == 0) {
                UiVideo(video, isSelected = true)
              } else {
                UiVideo(video, isSelected = false)
              }
            }
            .groupBy { it.video.type }

          val videos = mutableListOf<Any>().apply {
            groupedVideos.forEach { videoGroup ->
              add(UiVideoSection(videoGroup.key, videoGroup.value.size))
              addAll(videoGroup.value)
            }
          }

          val selectedPos = videos.indexOfFirst { it is UiVideo && it.isSelected }
          val selected = videos[selectedPos] as UiVideo
          setState {
            copy(videos = videos, selected = selected)
          }
        }
        is Result.Error -> setState { copy(videos = emptyList()) }
      }
    }
  }

  override fun onUiEvent(event: Event) {
    when (event) {
      is Event.VideoEnded -> {
        // Play next video in the list or if end is reached the first one
        val nextPosition = currentState.videos
          .indexOfFirst { item -> item is UiVideo && item.isSelected }
          .let { index -> if (index < currentState.videos.size - 1) index + 1 else 0 }
        val next = currentState.videos[nextPosition] as UiVideo

        setState {
          copy(videos = selectVideo(currentState.videos, next))
        }
      }
      is Event.VideoClicked -> {
        setState {
          copy(videos = selectVideo(currentState.videos, event.video), selected = event.video)
        }
      }
    }
  }

  private fun selectVideo(videos: List<Any>, video: UiVideo): List<Any> {
    val position = videos.indexOf(video).takeIf { pos -> pos != -1 } ?: return videos

    return videos.mapIndexed { index, item ->
      if (item is UiVideo) {
        if (index == position) {
          item.copy(isSelected = true)
        } else {
          item.copy(isSelected = false)
        }
      } else {
        item
      }
    }
  }

  data class State(val videos: List<Any> = emptyList(), val selected: UiVideo?)

  data class UiVideo(val video: Video, val isSelected: Boolean)

  data class UiVideoSection(val title: String, val count: Int)

  sealed class Event {
    object VideoEnded : Event()
    data class VideoClicked(val video: UiVideo) : Event()
  }
}