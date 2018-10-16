package com.illiarb.tmdbclient.feature.movies.details

import android.os.Bundle
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbclient.feature.movies.di.MoviesComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import com.illiarb.tmdbexplorer.coreui.state.UiState
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.ext.addTo
import kotlinx.android.synthetic.main.fragment_movie_details.*
import javax.inject.Inject

class MovieDetailsFragment : BaseFragment<MovieDetailsViewModel>(), Injectable {

    companion object {

        private const val EXTRA_MOVIE_ID = "id"

        fun newInstance(movieId: Int): MovieDetailsFragment {
            return MovieDetailsFragment().apply {
                arguments = Bundle()
                    .apply {
                        putInt(EXTRA_MOVIE_ID, movieId)
                    }
            }
        }
    }

    @Inject
    lateinit var infoPagerAdapter: MovieInfoPagerAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            val movieId = it.getInt(EXTRA_MOVIE_ID)

            viewModel.observeMovieDetailsState()
                .subscribe(::onMovieDetailsStateChanged, Throwable::printStackTrace)
                .addTo(destroyViewDisposable)

            viewModel.getMovieDetails(movieId)
        }
    }

    override fun getContentView(): Int = R.layout.fragment_movie_details

    override fun getViewModelClass(): Class<MovieDetailsViewModel> = MovieDetailsViewModel::class.java

    override fun inject(appProvider: AppProvider) {
        MoviesComponent.get(appProvider, requireActivity()).inject(this)
    }

    private fun onMovieDetailsStateChanged(uiState: UiState<Movie>) {
        if (uiState.hasData()) {
            showMovieDetails(uiState.requireData())
        }
    }

    private fun showMovieDetails(movie: Movie) {
        movie.backdropPath?.let {
            ImageLoader.loadImage(movieDetailsPoster, it)
        }

        movieDetailsTitle.text = movie.title

        if (movie.genres.isNotEmpty()) {
            val builder = StringBuilder()

            val it = movie.genres.iterator()
            while (it.hasNext()) {
                builder.append(it.next().name)

                if (it.hasNext()) {
                    builder.append(",")
                    builder.append(" ")
                }
            }

            movieDetailsTags.text = builder
        }

        infoPagerAdapter.movie = movie

        movieDetailsViewPager.adapter = infoPagerAdapter
        movieDetailsTabs.setupWithViewPager(movieDetailsViewPager)
    }
}