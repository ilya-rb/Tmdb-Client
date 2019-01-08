package com.illiarb.tmdbclient.feature.search.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.illiarb.tmdbclient.feature.search.R
import com.illiarb.tmdbclient.feature.search.SearchViewState
import com.illiarb.tmdbclient.feature.search.di.SearchComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.navigation.Router
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_empty_view.*
import javax.inject.Inject

/**
 * @author ilya-rb on 26.12.18.
 */
class SearchFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var searchAdapter: SearchAdapter

    @Inject
    lateinit var router: Router

    override fun getContentView(): Int = R.layout.fragment_search

    override fun inject(appProvider: AppProvider) = SearchComponent.get(appProvider).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchResultsList.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun render(state: SearchViewState) {
        searchProgress.visibility = if (state.isSearchRunning) View.VISIBLE else View.GONE

        val drawable =
            if (searchQuery.text.isNullOrEmpty()) {
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_search)
            } else {
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_clear_search)
            }
        searchIcon.setImageDrawable(drawable)

        searchAdapter.submitList(state.searchResults)

        if (state.searchResults.isEmpty()) {
            emptyView.visibility = View.VISIBLE
            searchResultsList.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            searchResultsList.visibility = View.VISIBLE
        }
    }
}