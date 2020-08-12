package com.illiarb.tmdbclient.functional.tests

import android.Manifest
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.illiarb.tmdbclient.functional.screens.DiscoverScreen
import com.illiarb.tmdbclient.functional.screens.HomeScreen
import com.illiarb.tmdbclient.functional.screens.HomeScreen.GenreSectionItem
import com.illiarb.tmdbclient.functional.screens.HomeScreen.NowPlayingSectionItem
import com.illiarb.tmdbclient.functional.screens.HomeScreen.NowPlayingSectionItem.NowPlayingItem
import com.illiarb.tmdbclient.functional.screens.HomeScreen.TrendingSectionItem
import com.illiarb.tmdbclient.functional.screens.MovieDetailsScreen
import com.illiarb.tmdbclient.modules.main.MainActivity
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.After
import org.junit.Rule
import org.junit.Test

class HomeScreenTest : TestCase(Kaspresso.Builder.simple()) {

  @get:Rule
  val runtimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE
  )

  @get:Rule
  val activityTestRule = activityScenarioRule<MainActivity>()

  @After
  fun cleanup() {
    activityTestRule.scenario.close()
  }

  @Test
  fun testHomeSectionsAreDisplayed() = run {
    before {}.after {}.run {
      step("Check home screen is visible") {
        HomeScreen {
          checkIsVisible()
        }
      }

      step("Check home sections is displayed") {
        HomeScreen {
          moviesList.firstChild<NowPlayingSectionItem> { isVisible() }
          moviesList.firstChild<GenreSectionItem> { isVisible() }
          moviesList.firstChild<TrendingSectionItem> { isVisible() }
        }
      }
    }
  }

  @Test
  fun testNowPlayingSectionMovieClickOpensMovieDetailsScreen() = run {
    before {}.after {}.run {
      step("click now playing section first item and check movie details screen is visible") {
        HomeScreen {
          moviesList.firstChild<NowPlayingSectionItem> {
            items.firstChild<NowPlayingItem> {
              isCompletelyDisplayed()
              click()

              MovieDetailsScreen {
                poster.isVisible()
              }
            }
          }
        }
      }
    }
  }

  @Test
  fun testGenreClickOpensDiscoverScreen() = run {
    before {}.after {}.run {
      step("Open home screen click first genre chip and check discover screen is displayed") {
        HomeScreen {
          moviesList.firstChild<GenreSectionItem> {
            genres.selectChipAt(0)

            DiscoverScreen {
              root.isVisible()
            }
          }
        }
      }
    }
  }
}