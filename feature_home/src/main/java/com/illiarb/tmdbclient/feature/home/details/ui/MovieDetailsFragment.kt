package com.illiarb.tmdbclient.feature.home.details.ui

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.ViewCompat
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.material.chip.Chip
import com.illiarb.tmdbclient.feature.home.MvpAppCompatFragment
import com.illiarb.tmdbclient.feature.home.R
import com.illiarb.tmdbclient.feature.home.details.MovieDetailsPresenter
import com.illiarb.tmdbclient.feature.home.details.MovieDetailsView
import com.illiarb.tmdbclient.feature.home.details.ui.photos.PhotosAdapter
import com.illiarb.tmdbclient.feature.home.details.ui.reviews.ReviewsAdapter
import com.illiarb.tmdbclient.feature.home.di.MoviesComponent
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.LayoutOrientation
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.LayoutType
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.RecyclerViewBuilder
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
    lateinit var imageLoader: ImageLoader

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieDetailsToolbar.apply {
            awareOfWindowInsets()
            movieDetailsToolbar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
        }

        RecyclerViewBuilder
            .create {
                adapter(photosAdapter)
                hasFixedSize(true)
                type(LayoutType.Linear)
                orientation(LayoutOrientation.HORIZONTAL)
                spaceBetween { horizontally = resources.getDimensionPixelSize(R.dimen.margin_small) / 2 }
            }
            .setupWith(movieDetailsPhotos)

        RecyclerViewBuilder
            .create {
                adapter(reviewsAdapter)
                type(LayoutType.Linear)
                disableNestedScroll()
                spaceBetween {
                    horizontally = resources.getDimensionPixelSize(R.dimen.margin_default)
                    vertically = resources.getDimensionPixelSize(R.dimen.margin_small)
                }
            }
            .setupWith(movieDetailsReviews)

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
                val options = ImageLoader.RequestOptions
                    .create {
                        cornerRadius(resources.getDimensionPixelSize(R.dimen.image_corner_radius))
                        cropOptions(ImageLoader.CropOptions.CENTER_CROP)
                    }

                imageLoader.fromUrl(it, movieDetailsPosterSmall, options)
                imageLoader.fromUrl(it, movieDetailsPoster, options.copy(cornerRadius = 0))
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