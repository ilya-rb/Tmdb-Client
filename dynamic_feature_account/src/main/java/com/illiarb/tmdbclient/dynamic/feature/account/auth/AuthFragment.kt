package com.illiarb.tmdbclient.dynamic.feature.account.auth

import com.illiarb.tmdbclient.dynamic.feature.account.R
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment

/**
 * @author ilya-rb on 20.11.18.
 */
class AuthFragment : BaseFragment<AuthViewModel>() {

    override fun getContentView(): Int = R.layout.fragment_auth

    override fun getViewModelClass(): Class<AuthViewModel> = AuthViewModel::class.java
}