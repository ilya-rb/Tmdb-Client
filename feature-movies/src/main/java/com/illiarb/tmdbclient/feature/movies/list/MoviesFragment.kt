package com.illiarb.tmdbclient.feature.movies.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbclient.feature.movies.di.MoviesComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter.AdapterDelegate
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter.DelegateAdapter
import com.illiarb.tmdbexplorer.coreui.state.UiState
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.MovieSection
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.system.EventBus
import kotlinx.android.synthetic.main.fragment_movies.*
import javax.inject.Inject

class MoviesFragment : BaseFragment<MoviesViewModel>(), Injectable {

    @Inject
    lateinit var delegateAdapter: DelegateAdapter

    @Inject
    lateinit var delegatesSet: Set<@JvmSuppressWildcards AdapterDelegate>

    @Inject
    lateinit var eventBus: EventBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Fade()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        delegateAdapter.addDelegatesFromSet(delegatesSet)

        moviesList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = delegateAdapter
            setHasFixedSize(true)
            addItemDecoration(MoviesSpaceDecoration(requireContext()))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.observeMovies()
            .subscribe(::onMoviesUiStateChanged, Throwable::printStackTrace)
            .addTo(destroyViewDisposable)

        eventBus.observeEvents(Movie::class.java)
            .subscribe({ viewModel.onMovieClicked(it) }, Throwable::printStackTrace)
            .addTo(destroyViewDisposable)
    }

    override fun getContentView(): Int = R.layout.fragment_movies

    override fun getViewModelClass(): Class<MoviesViewModel> = MoviesViewModel::class.java

    override fun inject(appProvider: AppProvider) = MoviesComponent.get(appProvider).inject(this)

    private fun onMoviesUiStateChanged(state: UiState<List<MovieSection>>) {
        if (state.hasData()) {
            delegateAdapter.submitList(state.requireData())
        }
    }
}