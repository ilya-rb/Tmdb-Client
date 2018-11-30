package com.illiarb.tmdbclient.dynamic.feature.account

import com.illiarb.tmdbexplorer.coreui.base.BaseFragment

/**
 * @author ilya-rb on 20.11.18.
 */
class AccountFragment : BaseFragment<AccountViewModel>() {

    override fun getContentView(): Int = R.layout.fragment_account

    override fun getViewModelClass(): Class<AccountViewModel> = AccountViewModel::class.java
}