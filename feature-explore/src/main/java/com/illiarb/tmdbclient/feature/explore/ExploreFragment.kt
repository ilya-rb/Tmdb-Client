package com.illiarb.tmdbclient.feature.explore

import com.illiarb.tmdbexplorer.coreui.base.BaseFragment

/**
 * @author ilya-rb on 31.10.18.
 */
class ExploreFragment : BaseFragment<ExploreViewModel>() {

    override fun getContentView(): Int = 0

    override fun getViewModelClass(): Class<ExploreViewModel> = ExploreViewModel::class.java
}