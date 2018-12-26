package com.illiarb.tmdbclient.feature.movies.list.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.illiarb.tmdbclient.feature.movies.MvpAppCompatFragment
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbclient.feature.movies.di.MoviesComponent
import com.illiarb.tmdbclient.feature.movies.list.MoviesPresenter
import com.illiarb.tmdbclient.feature.movies.list.MoviesView
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter.AdapterDelegate
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter.DelegateAdapter
import com.illiarb.tmdbexplorer.coreui.ext.awareOfWindowInsets
import com.illiarb.tmdbexplorer.coreui.pipeline.MoviePipelineData
import com.illiarb.tmdbexplorer.coreui.pipeline.UiPipelineData
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.entity.MovieSection
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.pipeline.EventPipeline
import kotlinx.android.synthetic.main.fragment_movies.*
import javax.inject.Inject

class MoviesFragment : MvpAppCompatFragment(), Injectable, MoviesView {

    @Inject
    lateinit var delegateAdapter: DelegateAdapter

    @Inject
    lateinit var delegatesSet: Set<@JvmSuppressWildcards AdapterDelegate>

    @Inject
    lateinit var uiEventsPipeline: EventPipeline<@JvmSuppressWildcards UiPipelineData>

    @Inject
    @InjectPresenter
    lateinit var presenter: MoviesPresenter

    @Suppress("unused")
    @ProvidePresenter
    fun providePresenter() = presenter

    override fun getContentView(): Int = R.layout.fragment_movies

    override fun inject(appProvider: AppProvider) = MoviesComponent.get(appProvider).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout.apply {
            isEnabled = false
            awareOfWindowInsets()
        }

        delegateAdapter.addDelegatesFromSet(delegatesSet)

        moviesList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = delegateAdapter
            setHasFixedSize(true)
            addItemDecoration(MoviesSpaceDecoration(requireContext()))
        }

        uiEventsPipeline.observeEvents()
            .ofType(MoviePipelineData::class.java)
            .subscribe({ presenter.onMovieClicked(it.movie) }, Throwable::printStackTrace)
            .addTo(destroyViewDisposable)

        ViewCompat.requestApplyInsets(view)
    }

    override fun showError(message: String) {
        showErrorDialog(message)
    }

    override fun showMovieSections(movies: List<MovieSection>) {
        delegateAdapter.submitList(movies)
    }

    override fun showProgress() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefreshLayout.isRefreshing = false
    }
}