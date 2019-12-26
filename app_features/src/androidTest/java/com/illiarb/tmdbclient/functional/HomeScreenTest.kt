package com.illiarb.tmdbclient.functional

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.illiarb.tmdbclient.functional.screens.HomeScreen
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class HomeScreeTest : TestCase(
    Kaspresso.Builder.default().apply {
        flakySafetyParams.timeoutMs = TimeUnit.SECONDS.toMillis(5)
    }
) {

    @Test
    fun test() = run {
        before { startMainActivity() }.after { }.run {
            step("Open home screen and check account icon is visible") {
                HomeScreen {
                    accountIcon { isVisible() }
                }
            }

            step("Open home screen and check list has now playing section") {
                HomeScreen {

                }
            }
        }
    }

    private fun startMainActivity() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        context.startActivity(Intent(Intent.ACTION_MAIN))
    }
}
