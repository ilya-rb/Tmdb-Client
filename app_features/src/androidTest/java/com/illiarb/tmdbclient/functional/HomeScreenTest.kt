package com.illiarb.tmdbclient.functional

import android.app.Activity
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class HomeScreeTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.default().apply {
        flakySafetyParams.timeoutMs = TimeUnit.SECONDS.toMillis(5)
    }
) {

    @get:Rule
    val activityTestRule = ActivityTestRule(Activity::class.java, true, false)

    @Test
    fun test() {
    }
}