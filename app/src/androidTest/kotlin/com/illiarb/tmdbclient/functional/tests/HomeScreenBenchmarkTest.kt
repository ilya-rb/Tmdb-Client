package com.illiarb.tmdbclient.functional.tests

import android.Manifest
import androidx.benchmark.junit4.BenchmarkRule
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.illiarb.tmdbclient.modules.main.MainActivity
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class HomeScreenBenchmarkTest : TestCase(Kaspresso.Builder.simple()) {

  @get:Rule
  val runtimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE
  )

  @get:Rule
  val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

  @get:Rule
  val benchmarkRule = BenchmarkRule()

  @Test
  fun benchmarkHomeSectionsRender() = run {
    before {
      activityTestRule.launchActivity(null)
    }.after {
    }.run {

    }
  }
}