package com.illiarb.tmdbclient.feature.movies.details

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbclient.feature.movies.di.MoviesComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.ext.awareOfWindowInsetsWithMargin
import com.illiarb.tmdbexplorer.coreui.ext.setTranslucentStatusBar
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import com.illiarb.tmdbexplorer.coreui.state.UiState
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.navigation.NavigationExtras
import kotlinx.android.synthetic.main.fragment_movie_details.*
import javax.inject.Inject

class MovieDetailsFragment : BaseFragment<MovieDetailsViewModel>(), Injectable {

    @Inject
    lateinit var infoPagerAdapter: MovieInfoPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity() as AppCompatActivity

        movieDetailsToolbar
            .apply {
                awareOfWindowInsetsWithMargin(R.dimen.movie_details_horizontal_margin)
                setNavigationOnClickListener {
                    requireActivity().onBackPressed()
                }
            }
            .also { activity.setSupportActionBar(it) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            val movieId = it.getInt(NavigationExtras.EXTRA_MOVIE_DETAILS_ID)

            viewModel.id = movieId
            viewModel.observeMovieDetailsState()
                .subscribe(::onMovieDetailsStateChanged, Throwable::printStackTrace)
                .addTo(destroyViewDisposable)
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().setTranslucentStatusBar(true)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().setTranslucentStatusBar(false)
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
        movie.posterPath?.let {
            ImageLoader.loadImage(movieDetailsPoster, it, true)
            ImageLoader.loadImage(
                movieDetailsPosterSmall,
                it,
                true,
                resources.getDimensionPixelSize(R.dimen.movie_details_small_poster_corner_radius)
            )
        }

        movieDetailsToolbar.title = movie.title
        movieDetailsTitle.text = movie.title
        movieDetailsRating.text = movie.voteAverage.toString()

        movie.runtime?.let {
            movieDetailsDuration.text = getString(R.string.movie_details_duration, it)
        }

        movie.genres.forEach { genre ->
            Chip(requireContext(), null, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Action)
                .apply {
                    text = genre.name
                    setOnClickListener {
                        viewModel.onGenreClicked(text.toString())
                    }
                }
                .also { movieDetailsTags.addView(it) }
        }

        infoPagerAdapter.movie = movie

        movieDetailsViewPager.adapter = infoPagerAdapter
        movieDetailsViewPager.offscreenPageLimit = MovieInfoPagerAdapter.TABS_COUNT

        movieDetailsTabs.setupWithViewPager(movieDetailsViewPager)
    }
}