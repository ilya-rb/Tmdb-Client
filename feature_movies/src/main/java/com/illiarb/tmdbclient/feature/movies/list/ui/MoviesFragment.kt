package com.illiarb.tmdbclient.feature.movies.list.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import com.badoo.mvicore.binder.Binder
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbclient.feature.movies.di.MoviesComponent
import com.illiarb.tmdbclient.feature.movies.list.feature.MoviesFeature
import com.illiarb.tmdbclient.feature.movies.list.feature.MoviesState
import com.illiarb.tmdbexplorer.coreui.base.BaseMviFragment
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter.AdapterDelegate
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter.DelegateAdapter
import com.illiarb.tmdbexplorer.coreui.ext.awareOfWindowInsets
import com.illiarb.tmdbexplorer.coreui.pipeline.MoviePipelineData
import com.illiarb.tmdbexplorer.coreui.pipeline.UiPipelineData
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.navigation.MovieDetailsScreen
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.pipeline.EventPipeline
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_movies.*
import javax.inject.Inject

class MoviesFragment : BaseMviFragment(), Injectable, Consumer<MoviesState> {

    @Inject
    lateinit var delegateAdapter: DelegateAdapter

    @Inject
    lateinit var delegatesSet: Set<@JvmSuppressWildcards AdapterDelegate>

    @Inject
    lateinit var uiEventsPipeline: EventPipeline<@JvmSuppressWildcards UiPipelineData>

    @Inject
    lateinit var feature: MoviesFeature

    @Inject
    lateinit var router: Router

    private val binder = Binder()

    private val newsListener = Consumer<MoviesFeature.News> { news ->
        when (news) {
            is MoviesFeature.News.ShowMovieDetails -> router.navigateTo(MovieDetailsScreen(news.id))
        }
    }

    override fun getContentView(): Int = R.layout.fragment_movies

    override fun inject(appProvider: AppProvider) = MoviesComponent.get(appProvider).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Fade()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi(view)
        setupBindings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binder.clear()
    }

    override fun accept(state: MoviesState) {
        swipeRefreshLayout.isRefreshing = state.isLoading

        delegateAdapter.submitList(state.movies)

        state.error?.message?.let {
            showError(it)
        }
    }

    private fun setupUi(view: View) {
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
            itemAnimator?.changeDuration = 0
        }

        uiEventsPipeline.observeEvents()
            .ofType(MoviePipelineData::class.java)
            .subscribe({ feature.accept(MoviesFeature.Wish.ShowMovieDetails(it.movie.id)) }, Throwable::printStackTrace)
            .addTo(destroyViewDisposable)

        ViewCompat.requestApplyInsets(view)
    }

    private fun setupBindings() {
        binder.bind(feature to this)
        binder.bind(feature.news to newsListener)

        feature.accept(MoviesFeature.Wish.Refresh)
    }
}