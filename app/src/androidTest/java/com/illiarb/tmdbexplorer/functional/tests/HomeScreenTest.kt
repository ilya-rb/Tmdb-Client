package com.illiarb.tmdbexplorer.functional.tests

import android.Manifest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.illiarb.tmdbclient.MainActivity
import com.illiarb.tmdbexplorer.functional.screens.HomeScreen
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class HomeScreeTest : TestCase(
    Kaspresso.Builder.default().apply {
        flakySafetyParams.timeoutMs = TimeUnit.SECONDS.toMillis(5)
    }
) {

    @get:Rule
    val runtimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Test
    fun test() = run {
        before {
            activityTestRule.launchActivity(null)
        }.after {
        }.run {
            step("Open home screen and check settings icon is visible") {
                HomeScreen {
                    settingsIcon { isVisible() }
                }
            }
        }
    }
}
