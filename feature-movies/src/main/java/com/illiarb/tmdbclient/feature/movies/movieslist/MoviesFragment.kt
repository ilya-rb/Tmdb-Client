package com.illiarb.tmdbclient.feature.movies.movieslist

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbclient.feature.movies.di.MoviesComponent
import com.illiarb.tmdbclient.feature.movies.movieslist.adapter.MoviesAdapter
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.decoration.SpaceItemDecoration
import com.illiarb.tmdbexplorer.coreui.state.UiState
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.ext.addTo
import kotlinx.android.synthetic.main.fragment_movies.*
import javax.inject.Inject

class MoviesFragment : BaseFragment<MoviesViewModel>(), Injectable {

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moviesList.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = moviesAdapter
            it.setHasFixedSize(true)

            val spacing = resources.getDimensionPixelSize(R.dimen.item_movie_spacing)
            it.addItemDecoration(SpaceItemDecoration(spacing, spacing))
        }

        moviesFilter.setOnClickListener {
            viewModel.onFilterClicked()
        }

        swipeRefreshLayout.isEnabled = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.observeMovies()
            .subscribe(::onMoviesUiStateChanged, Throwable::printStackTrace)
            .addTo(destroyViewDisposable)

        moviesAdapter.clickEvent = { _, _, movie -> viewModel.onMovieClicked(movie) }
    }

    override fun getContentView(): Int = R.layout.fragment_movies

    override fun getViewModelClass(): Class<MoviesViewModel> = MoviesViewModel::class.java

    override fun inject(appProvider: AppProvider) {
        MoviesComponent.get(appProvider, requireActivity()).inject(this)
    }

    private fun onMoviesUiStateChanged(state: UiState<List<Movie>>) {
        swipeRefreshLayout.isRefreshing = state.isLoading()

        if (state.hasData()) {
            moviesAdapter.submitList(state.requireData())
        }
    }
}