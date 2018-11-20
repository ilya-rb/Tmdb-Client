package com.illiarb.tmdbclient.dynamic.feature.account

import android.view.View
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment

/**
 * @author ilya-rb on 20.11.18.
 */
class AccountFragment : BaseFragment<AccountViewModel>() {

    override fun getContentView(): Int = View.NO_ID

    override fun getViewModelClass(): Class<AccountViewModel> = AccountViewModel::class.java
}