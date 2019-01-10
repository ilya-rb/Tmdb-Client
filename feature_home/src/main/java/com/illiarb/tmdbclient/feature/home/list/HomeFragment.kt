package com.illiarb.tmdbclient.feature.home.list

import android.os.Bundle
import android.view.View
import com.illiarb.tmdbclient.feature.home.R
import com.illiarb.tmdbclient.feature.home.di.MoviesComponent
import com.illiarb.tmdbexplorer.coreui.StateObserver
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.recyclerview.LayoutType
import com.illiarb.tmdbexplorer.coreui.recyclerview.RecyclerViewBuilder
import com.illiarb.tmdbexplorer.coreui.recyclerview.adapter.AdapterDelegate
import com.illiarb.tmdbexplorer.coreui.recyclerview.adapter.DelegateAdapter
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.entity.Movie
import kotlinx.android.synthetic.main.fragment_movies.*
import javax.inject.Inject

class HomeFragment : BaseFragment(), Injectable, StateObserver<HomeState> {

    @Inject
    lateinit var delegateAdapter: DelegateAdapter

    @Inject
    lateinit var homeModel: HomeModel

    @Inject
    lateinit var delegatesSet: Set<@JvmSuppressWildcards AdapterDelegate>

    override fun getContentView(): Int = R.layout.fragment_movies

    override fun inject(appProvider: AppProvider) = MoviesComponent.get(appProvider).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RecyclerViewBuilder
            .create {
                adapter(delegateAdapter.also { it.addDelegatesFromSet(delegatesSet) })
                type(LayoutType.Linear())
                hasFixedSize(true)
                spaceBetween {
                    horizontally = resources.getDimensionPixelSize(R.dimen.item_movie_spacing)
                    addToFirst = true
                }
            }
            .setupWith(moviesList)

        delegateAdapter.setClickEvent { _, _, item ->
            when (item) {
                is Movie -> homeModel.onMovieClick(item)
            }
        }

        homeSearch.setOnClickListener {
            homeModel.onSearchClick()
        }

        homeAccount.setOnClickListener {
            homeModel.onAccountClick()
        }
    }

    override fun onStart() {
        super.onStart()
        homeModel.getStateObservable().addObserver(this)
    }

    override fun onStop() {
        super.onStop()
        homeModel.getStateObservable().removeObserver(this)
    }

    override fun onStateChanged(state: HomeState) {
        movieProgress.visibility = if (state.isLoading) View.VISIBLE else View.GONE

        if (state.movies.isNotEmpty()) {
            delegateAdapter.submitList(state.movies)
        }
    }
}