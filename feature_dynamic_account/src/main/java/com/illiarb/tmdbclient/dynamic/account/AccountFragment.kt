package com.illiarb.tmdbclient.dynamic.account

import android.view.View
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment

/**
 * @author ilya-rb on 19.11.18.
 */
class AccountFragment : BaseFragment<AccountViewModel>() {

    override fun getContentView(): Int = View.NO_ID

    override fun getViewModelClass(): Class<AccountViewModel> = AccountViewModel::class.java
}