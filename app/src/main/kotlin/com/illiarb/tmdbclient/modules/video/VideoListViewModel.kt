package com.illiarb.tmdbclient.modules.video

import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbclient.libs.ui.base.viewmodel.BaseViewModel
import com.illiarb.tmdbclient.libs.ui.common.ErrorMessage
import com.illiarb.tmdbclient.libs.ui.common.ViewStateEvent
import com.illiarb.tmdbclient.libs.util.Result
import com.illiarb.tmdbclient.navigation.NavigationAction
import com.illiarb.tmdbclient.navigation.Router
import com.illiarb.tmdbclient.services.tmdb.domain.Video
import com.illiarb.tmdbclient.services.tmdb.interactor.MoviesInteractor
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoListViewModel @Inject constructor(
  private var movieId: Int,
  private val moviesInteractor: MoviesInteractor,
  private val router: Router
) : BaseViewModel<VideoListViewModel.State, VideoListViewModel.Event>(initialState()) {

  companion object {
    private fun initialState() = State(
      videos = emptyList(),
      selected = null,
      error = null
    )
  }

  init {
    init(movieId)
  }

  override fun onUiEvent(event: Event) {
    when (event) {
      is Event.CloseClicked -> router.executeAction(NavigationAction.CloseVideoList)
      is Event.NewMovieSelected -> {
        movieId = event.movieId
        init(movieId)
      }
      is Event.VideoClicked -> {
        setState {
          copy(videos = selectVideo(currentState.videos, event.video), selected = event.video)
        }
      }
    }
  }

  private fun init(movieId: Int) {
    viewModelScope.launch {
      when (val result = moviesInteractor.getMovieVideos(movieId)) {
        is Result.Ok -> {
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
        is Result.Err -> setState {
          copy(
            videos = emptyList(),
            error = ViewStateEvent(ErrorMessage(result.error.message ?: ""))
          )
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

  data class State(
    val videos: List<Any> = emptyList(),
    val selected: UiVideo?,
    val error: ViewStateEvent<ErrorMessage>?
  )

  data class UiVideo(val video: Video, val isSelected: Boolean)

  data class UiVideoSection(val title: String, val count: Int)

  sealed class Event {
    data class VideoClicked(val video: UiVideo) : Event()
    data class NewMovieSelected(val movieId: Int) : Event()
    object CloseClicked : Event()
  }
}