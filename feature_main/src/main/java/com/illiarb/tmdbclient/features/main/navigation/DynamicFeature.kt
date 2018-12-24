package com.illiarb.tmdbclient.features.main.navigation

import androidx.fragment.app.Fragment

/**
 * @author ilya-rb on 24.12.18.
 */
object DynamicFeature {

    private const val DYNAMIC_FEATURE_PACKAGE = "com.illiarb.tmdbclient.dynamic.feature"

    interface DynamicFragment {
        val className: Class<out Fragment>
    }

    object Account : DynamicFragment {
        override val className: Class<out Fragment> =
            Class
                .forName("$DYNAMIC_FEATURE_PACKAGE.account.profile.ui.AccountFragment")
                .asSubclass(Fragment::class.java)
    }

    object Auth : DynamicFragment {
        override val className: Class<out Fragment> =
            Class
                .forName("$DYNAMIC_FEATURE_PACKAGE.account.auth.ui.AuthFragment")
                .asSubclass(Fragment::class.java)
    }
}