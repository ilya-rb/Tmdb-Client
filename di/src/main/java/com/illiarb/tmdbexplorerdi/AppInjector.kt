package com.illiarb.tmdbexplorerdi

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

class AppInjector(private val app: App) : Application.ActivityLifecycleCallbacks {

    fun registerLifecycleCallbacks() {
        app.getApplication().registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is Injectable) {
            activity.inject(app.getAppProvider())
        }

        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks(), true)
        }
    }

    override fun onActivityPaused(activity: Activity?) {
    }

    override fun onActivityResumed(activity: Activity?) {
    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
    }

    private fun fragmentLifecycleCallbacks(): FragmentManager.FragmentLifecycleCallbacks {
        return object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentPreAttached(fm: FragmentManager, fragment: Fragment, context: Context) {
                if (fragment is Injectable) {
                    fragment.inject(app.getAppProvider())
                }
            }
        }
    }
}