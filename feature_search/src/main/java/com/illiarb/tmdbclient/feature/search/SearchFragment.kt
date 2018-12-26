package com.illiarb.tmdbclient.feature.search

import com.illiarb.tmdbclient.feature.search.di.SearchComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider

/**
 * @author ilya-rb on 26.12.18.
 */
class SearchFragment : BaseFragment(), Injectable {

    override fun getContentView(): Int = R.layout.fragment_search

    override fun inject(appProvider: AppProvider) = SearchComponent.get(appProvider).inject(this)
}