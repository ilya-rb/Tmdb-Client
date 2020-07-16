package com.illiarb.tmdbclient.functional.tests

import android.Manifest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.illiarb.tmdbclient.functional.recyclerview.MovieItem
import com.illiarb.tmdbclient.functional.screens.DiscoverScreen
import com.illiarb.tmdbclient.functional.screens.HomeScreen
import com.illiarb.tmdbclient.functional.screens.MovieDetailsScreen
import com.illiarb.tmdbclient.modules.main.MainActivity
import com.illiarb.tmdbclient.services.tmdb.domain.Genre
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class DiscoverScreenTest : TestCase(Kaspresso.Builder.simple()) {

  @get:Rule
  val runtimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE
  )

  @get:Rule
  val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

  @Test
  fun testShouldOpenFilterOnFiltersPanelSwipe() = run {
    before {
      activityTestRule.launchActivity(null)
    }.after {
    }.run {
      step("Open discover screen") {
        HomeScreen {
          openDiscoverScreen()
        }
      }

      step("Swipe filters panel") {
        DiscoverScreen {
//          swipeFiltersToTop()
        }
      }

      step("Check genres are displayed") {
        DiscoverScreen {
          //filterGenres.isCompletelyDisplayed()
        }
      }
    }
  }

  @Test
  fun testShouldSelectGenreFilterOnChipClick() = run {
    before {
      activityTestRule.launchActivity(null)
    }.after {
    }.run {
      val selectedFilterGenre = Genre.DRAMA.capitalize()

      step("Open discover screen") {
        HomeScreen {
          openDiscoverScreen()
        }
      }

      step("Swipe filters panel") {
        DiscoverScreen {
          //swipeFiltersToTop()
        }
      }

      step("Click first genre chip") {
        DiscoverScreen {
          //filterGenres.selectChip(selectedFilterGenre)
//          applyFilter.click()
        }
      }

      step("Check toolbar title is changed to selected filter") {
        DiscoverScreen {
          toolbar.hasDescendant {
            withText(selectedFilterGenre)
          }
        }
      }
    }
  }

  @Test
  fun testShouldShowMovieDetailsOnMovieClick() = run {
    before {
      activityTestRule.launchActivity(null)
    }.after {
    }.run {
      step("Open discover screen") {
        HomeScreen {
          openDiscoverScreen()
        }
      }

      step("Click first item in the list") {
        DiscoverScreen {
          recyclerView.firstChild<MovieItem> {
            click()
          }
        }
      }

      step("Check movie details screen is displayed") {
        MovieDetailsScreen {
          poster.isDisplayed()
        }
      }
    }
  }
}