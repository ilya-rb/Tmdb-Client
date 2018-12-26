package com.illiarb.tmdbclient.feature.movies.details.ui

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Slide
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.material.chip.Chip
import com.illiarb.tmdbclient.feature.movies.MvpAppCompatFragment
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbclient.feature.movies.details.MovieDetailsPresenter
import com.illiarb.tmdbclient.feature.movies.details.MovieDetailsView
import com.illiarb.tmdbclient.feature.movies.details.ui.photos.PhotosAdapter
import com.illiarb.tmdbclient.feature.movies.details.ui.reviews.ReviewsAdapter
import com.illiarb.tmdbclient.feature.movies.di.MoviesComponent
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.decoration.SpaceItemDecoration
import com.illiarb.tmdbexplorer.coreui.ext.awareOfWindowInsets
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.navigation.NavigationKeys
import kotlinx.android.synthetic.main.fragment_movie_details.*
import javax.inject.Inject

class MovieDetailsFragment : MvpAppCompatFragment(), Injectable, MovieDetailsView {

    @Inject
    lateinit var photosAdapter: PhotosAdapter

    @Inject
    lateinit var reviewsAdapter: ReviewsAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: MovieDetailsPresenter

    private val containerScrollListener by lazy(LazyThreadSafetyMode.NONE) {
        ViewTreeObserver.OnScrollChangedListener {
            if (movieDetailsContainer.scrollY > 0) {
                movieDetailsPlayTrailer.hide()
            } else {
                movieDetailsPlayTrailer.show()
            }
        }
    }

    @Suppress("unused")
    @ProvidePresenter
    fun providePresenter(): MovieDetailsPresenter =
        presenter.apply {
            arguments?.let { arguments ->
                id = arguments.getInt(NavigationKeys.EXTRA_MOVIE_DETAILS_ID)
            }
        }

    override fun getContentView(): Int = R.layout.fragment_movie_details

    override fun inject(appProvider: AppProvider) = MoviesComponent.get(appProvider).inject(this)

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

        movieDetailsPhotos.apply {
            adapter = photosAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(
                SpaceItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.margin_small) / 2,
                    0,
                    false,
                    false
                )
            )
        }

        movieDetailsReviews.apply {
            adapter = reviewsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            isNestedScrollingEnabled = false
            addItemDecoration(
                SpaceItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.margin_default),
                    resources.getDimensionPixelSize(R.dimen.margin_small)
                )
            )
        }

        ViewCompat.requestApplyInsets(view)
    }

    override fun onResume() {
        super.onResume()
        movieDetailsContainer.viewTreeObserver.addOnScrollChangedListener(containerScrollListener)
    }

    override fun onPause() {
        super.onPause()
        movieDetailsContainer.viewTreeObserver.removeOnScrollChangedListener(containerScrollListener)
    }

    override fun showError(message: String) {
        showErrorDialog(message)
    }

    override fun showMovieDetails(movie: Movie) {
        with(movie) {
            movieDetailsTitle.text = title

            posterPath?.let {
                ImageLoader.loadImage(movieDetailsPoster, it, true)
                ImageLoader.loadImage(
                    movieDetailsPosterSmall,
                    it,
                    true,
                    resources.getDimensionPixelSize(R.dimen.image_corner_radius)
                )
            }

            movieDetailsRating.text = voteAverage.toString()

            if (runtime > 0) {
                movieDetailsDuration.text = getString(R.string.movie_details_duration, runtime)
            }

            genres.forEach { genre ->
                Chip(requireContext(), null, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Action)
                    .apply {
                        text = genre.name
                        setOnClickListener {
                            presenter.onGenreClicked(genre.id)
                        }
                    }
                    .also { movieDetailsTags.addView(it) }
            }

            overview?.let {
                movieDetailsOverview.text = it
            }

            if (images.isNotEmpty()) {
                movieDetailsPhotosTitle.visibility = View.VISIBLE
                photosAdapter.submitList(images)
            }

            if (reviews.isNotEmpty()) {
                movieDetailsReviewsTitle.visibility = View.VISIBLE
                reviewsAdapter.submitList(reviews)
            }
        }
    }
}