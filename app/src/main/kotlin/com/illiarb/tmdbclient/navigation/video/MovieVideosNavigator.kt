package com.illiarb.tmdbclient.navigation.video

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.di.modules.NavigationModule.DefaultVideosFragmentClassName
import com.illiarb.tmdbclient.navigation.NavigationAction
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieVideosNavigator @Inject constructor(
  private val activity: FragmentActivity,
  @DefaultVideosFragmentClassName
  private val defaultVideosFragmentClassName: Class<out @JvmSuppressWildcards Fragment>
) {

  fun executeVideoListAction(action: NavigationAction.VideoList) {
    activity.lifecycleScope.launch {
      activity.lifecycle.whenResumed {
        val videoContainerChildFragment =
          activity.supportFragmentManager.findFragmentById(R.id.videoFragmentContainer)

        if (videoContainerChildFragment == null) {
          activity.supportFragmentManager.beginTransaction()
            .add(
              R.id.videoFragmentContainer,
              defaultVideosFragmentClassName,
              Bundle().apply {
                putInt(NavigationAction.EXTRA_VIDEOS_MOVIE_ID, action.movieId)
              }
            )
            .commitNow()
        } else {
          require(videoContainerChildFragment is CanPlayMovieVideos) {
            "videoListFragmentContainer should only contain fragments with VideoContainerChild interface implemented"
          }
          videoContainerChildFragment.loadVideos(action.movieId)
        }
      }
    }
  }

  fun executeCloseVideoListAction() {
    activity.lifecycleScope.launch {
      activity.lifecycle.whenResumed {
        activity.supportFragmentManager.findFragmentById(R.id.videoFragmentContainer)
          ?.let { fragment ->
            activity.supportFragmentManager.beginTransaction()
              .remove(fragment)
              .commit()
          }
      }
    }
  }
}