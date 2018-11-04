package com.illiarb.tmdbclient.feature.movies.movieslist

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbclient.feature.movies.di.MoviesComponent
import com.illiarb.tmdbclient.feature.movies.movieslist.adapter.MovieSectionDelegate
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter.DelegateAdapter
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.decoration.SpaceItemDecoration
import com.illiarb.tmdbexplorer.coreui.state.UiState
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.MovieSection
import com.illiarb.tmdblcient.core.ext.addTo
import kotlinx.android.synthetic.main.fragment_movies.*
import javax.inject.Inject

class MoviesFragment : BaseFragment<MoviesViewModel>(), Injectable {

    @Inject
    lateinit var delegateAdapter: DelegateAdapter

    @Inject
    lateinit var movieSectionDelegate: MovieSectionDelegate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        delegateAdapter.addDelegate(movieSectionDelegate)

        moviesList.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = delegateAdapter
            it.setHasFixedSize(true)

            val spacing = resources.getDimensionPixelSize(R.dimen.item_movie_spacing)
            it.addItemDecoration(SpaceItemDecoration(spacing / 2, spacing / 2))
        }

        swipeRefreshLayout.isEnabled = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.observeMovies()
            .subscribe(::onMoviesUiStateChanged, Throwable::printStackTrace)
            .addTo(destroyViewDisposable)

        delegateAdapter.clickEvent = { _, _, item ->
            if (item is Movie) {
                viewModel.onMovieClicked(item)
            }
        }
    }

    override fun getContentView(): Int = R.layout.fragment_movies

    override fun getViewModelClass(): Class<MoviesViewModel> = MoviesViewModel::class.java

    override fun inject(appProvider: AppProvider) {
        MoviesComponent.get(appProvider, requireActivity()).inject(this)
    }

    private fun onMoviesUiStateChanged(state: UiState<List<MovieSection>>) {
        swipeRefreshLayout.isRefreshing = state.isLoading()

        if (state.hasData()) {
            delegateAdapter.submitList(state.requireData())
        }
    }
}