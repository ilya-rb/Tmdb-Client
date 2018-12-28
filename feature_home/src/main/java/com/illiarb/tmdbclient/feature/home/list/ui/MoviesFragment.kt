package com.illiarb.tmdbclient.feature.home.list.ui

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.material.chip.Chip
import com.illiarb.tmdbclient.feature.home.MvpAppCompatFragment
import com.illiarb.tmdbclient.feature.home.R
import com.illiarb.tmdbclient.feature.home.di.MoviesComponent
import com.illiarb.tmdbclient.feature.home.list.MoviesPresenter
import com.illiarb.tmdbclient.feature.home.list.MoviesView
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.LayoutType
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.RecyclerViewBuilder
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter.AdapterDelegate
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter.DelegateAdapter
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

        RecyclerViewBuilder
            .create {
                adapter(delegateAdapter.also { it.addDelegatesFromSet(delegatesSet) })
                type(LayoutType.Linear)
                hasFixedSize(true)
                spaceBetween {
                    horizontally = resources.getDimensionPixelSize(R.dimen.item_movie_spacing)
                }
            }
            .attachToRecyclerView(moviesList)

        movieFilters
            .addView(
                Chip(requireContext(), null, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Action)
                    .apply { text = "Now Playing" }
            )

        movieFilters
            .addView(
                Chip(requireContext(), null, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Action)
                    .apply { text = "Upcoming" }
            )

        uiEventsPipeline.observeEvents()
            .ofType(MoviePipelineData::class.java)
            .map(MoviePipelineData::movie)
            .subscribe(presenter::onMovieClicked, Throwable::printStackTrace)
            .addTo(destroyViewDisposable)
    }

    override fun showError(message: String) {
        showErrorDialog(message)
    }

    override fun showMovieSections(movies: List<MovieSection>) {
        delegateAdapter.submitList(movies)
    }

    override fun showProgress() {
        movieProgress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        movieProgress.visibility = View.GONE
    }
}