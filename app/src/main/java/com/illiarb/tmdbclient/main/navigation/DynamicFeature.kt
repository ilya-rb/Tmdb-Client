package com.illiarb.tmdbclient.main.navigation

/**
 * @author ilya-rb on 24.12.18.
 */
object DynamicFeature {

    private const val DYNAMIC_FEATURE_PACKAGE = "com.illiarb.tmdbclient.dynamic.feature"

    interface DynamicFragment {
        val className: String
    }

    object Account : DynamicFragment {
        override val className = "$DYNAMIC_FEATURE_PACKAGE.account.profile.ui.AccountFragment"
    }

    object Auth : DynamicFragment {
        override val className = "$DYNAMIC_FEATURE_PACKAGE.account.auth.ui.AuthFragment"
    }
}