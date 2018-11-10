package com.illiarb.tmdbclient.feature.movies.details

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Slide
import com.google.android.material.chip.Chip
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbclient.feature.movies.details.photos.PhotosAdapter
import com.illiarb.tmdbclient.feature.movies.details.reviews.ReviewsAdapter
import com.illiarb.tmdbclient.feature.movies.di.MoviesComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.decoration.SpaceItemDecoration
import com.illiarb.tmdbexplorer.coreui.ext.awareOfWindowInsets
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
    lateinit var photosAdapter: PhotosAdapter

    @Inject
    lateinit var reviewsAdapter: ReviewsAdapter

    private val containerScrollListener by lazy(LazyThreadSafetyMode.NONE) {
        ViewTreeObserver.OnScrollChangedListener {
            if (movieDetailsContainer.scrollY > 0) {
                movieDetailsPlayTrailer.hide()
            } else {
                movieDetailsPlayTrailer.show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = Slide(Gravity.END)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieDetailsToolbar.apply {
            awareOfWindowInsets()
            movieDetailsToolbar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
        }

        movieDetailsPhotos.let {
            it.adapter = photosAdapter
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            it.addItemDecoration(
                SpaceItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.movie_details_horizontal_small_margin) / 2,
                    0,
                    false,
                    false
                )
            )
        }

        movieDetailsReviews.let {
            it.adapter = reviewsAdapter
            it.layoutManager = LinearLayoutManager(requireContext())
            it.addItemDecoration(SpaceItemDecoration(0, resources.getDimensionPixelSize(R.dimen.movie_details_horizontal_small_margin)))
            it.isNestedScrollingEnabled = false
        }

        ViewCompat.requestApplyInsets(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            val movieId = it.getInt(NavigationExtras.EXTRA_MOVIE_DETAILS_ID)

            val title = it.getString(NavigationExtras.EXTRA_MOVIE_DETAILS_TITLE, "")
            val posterPath = it.getString(NavigationExtras.EXTRA_MOVIE_DETAILS_POSTER)
            showMovieDetailsBasicInfo(title, posterPath)

            viewModel.id = movieId
            viewModel.observeMovieDetailsState()
                .subscribe(::onMovieDetailsStateChanged, Throwable::printStackTrace)
                .addTo(destroyViewDisposable)
        }
    }

    override fun onResume() {
        super.onResume()
        movieDetailsContainer.viewTreeObserver.addOnScrollChangedListener(containerScrollListener)
    }

    override fun onPause() {
        super.onPause()
        movieDetailsContainer.viewTreeObserver.removeOnScrollChangedListener(containerScrollListener)
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
        showMovieDetailsBasicInfo(movie.title, movie.posterPath)

        with(movie) {
            movieDetailsRating.text = voteAverage.toString()

            runtime?.let {
                movieDetailsDuration.text = getString(R.string.movie_details_duration, it)
            }

            genres.forEach { genre ->
                Chip(requireContext(), null, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Action)
                    .apply {
                        text = genre.name
                        setOnClickListener {
                            viewModel.onGenreClicked(text.toString())
                        }
                    }
                    .also { movieDetailsTags.addView(it) }
            }

            overview?.let {
                movieDetailsOverview.text = it
            }

            photosAdapter.submitList(images.map { it.filePath })
            reviewsAdapter.submitList(reviews)
        }
    }

    private fun showMovieDetailsBasicInfo(title: String, posterPath: String?) {
        movieDetailsTitle.text = title

        posterPath?.let {
            ImageLoader.loadImage(movieDetailsPoster, it, true)
            ImageLoader.loadImage(
                movieDetailsPosterSmall,
                it,
                true,
                resources.getDimensionPixelSize(R.dimen.movie_details_small_poster_corner_radius)
            )
        }
    }
}