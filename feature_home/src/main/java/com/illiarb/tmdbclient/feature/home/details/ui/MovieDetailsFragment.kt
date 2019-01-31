package com.illiarb.tmdbclient.feature.home.details.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.google.android.material.chip.Chip
import com.illiarb.tmdbclient.feature.home.R
import com.illiarb.tmdbclient.feature.home.details.presentation.MovieDetailsModel
import com.illiarb.tmdbclient.feature.home.details.presentation.MovieDetailsUiState
import com.illiarb.tmdbclient.feature.home.details.ui.photos.PhotosAdapter
import com.illiarb.tmdbclient.feature.home.details.ui.reviews.ReviewsAdapter
import com.illiarb.tmdbclient.feature.home.di.MoviesComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.ext.addToViewGroup
import com.illiarb.tmdbexplorer.coreui.ext.awareOfWindowInsets
import com.illiarb.tmdbexplorer.coreui.ext.hide
import com.illiarb.tmdbexplorer.coreui.ext.show
import com.illiarb.tmdbexplorer.coreui.image.CropOptions
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import com.illiarb.tmdbexplorer.coreui.image.RequestOptions
import com.illiarb.tmdbexplorer.coreui.recyclerview.LayoutOrientation
import com.illiarb.tmdbexplorer.coreui.recyclerview.LayoutType
import com.illiarb.tmdbexplorer.coreui.recyclerview.RecyclerViewBuilder
import com.illiarb.tmdbexplorer.coreui.util.LawObserver
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.navigation.MovieDetailsScreen
import com.illiarb.tmdblcient.core.navigation.PhotoViewScreen
import com.illiarb.tmdblcient.core.navigation.Router
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

@ExperimentalCoroutinesApi
class MovieDetailsFragment : BaseFragment<MovieDetailsModel>(), Injectable {

    @Inject
    lateinit var photosAdapter: PhotosAdapter

    @Inject
    lateinit var reviewsAdapter: ReviewsAdapter

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var router: Router

    private val stateObserver: LawObserver<MovieDetailsUiState> by lazy(NONE) {
        LawObserver.create(presentationModel.stateObservable(), ::render)
    }

    override fun getContentView(): Int = R.layout.fragment_movie_details

    override fun inject(appProvider: AppProvider) = MoviesComponent.get(appProvider).inject(this)

    override fun getModelClass(): Class<MovieDetailsModel> = MovieDetailsModel::class.java

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
                type(LayoutType.Linear())
                orientation(LayoutOrientation.HORIZONTAL)
                spaceBetween { spacing = resources.getDimensionPixelSize(R.dimen.margin_small) / 2 }
            }
            .setupWith(movieDetailsPhotos)

        photosAdapter.clickEvent = { clickedView, _, photo ->
            val photos = photosAdapter.readOnlyList()
            val screenData =
                PhotoViewScreen(clickedView, photos, photo)
            router.navigateTo(screenData)
        }

        RecyclerViewBuilder
            .create {
                adapter(reviewsAdapter)
                type(LayoutType.Linear())
                spaceBetween {
                    spacing = resources.getDimensionPixelSize(R.dimen.margin_small)
                }
            }
            .setupWith(movieDetailsReviews)

        ViewCompat.requestApplyInsets(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        stateObserver.register(this)

        arguments?.let {
            val id = it.getInt(MovieDetailsScreen.EXTRA_ID)
            presentationModel.getMovieDetails(id)
        }
    }

    private fun render(state: MovieDetailsUiState) {
        state.movie?.let {
            showMovieDetails(it)
        }
    }

    private fun showMovieDetails(movie: Movie) = runOnUi {
        with(movie) {
            movieDetailsTitle.text = title

            posterPath?.let {
                val options = RequestOptions.create {
                    cornerRadius(resources.getDimensionPixelSize(R.dimen.image_corner_radius))
                    cropOptions(CropOptions.CENTER_CROP)
                }

                imageLoader.fromUrl(it, movieDetailsPosterSmall, options)
                imageLoader.fromUrl(it, movieDetailsPoster, options.copy(cornerRadius = 0))
            }

            movieDetailsRating.text = voteAverage.toString()

            if (runtime > 0) {
                movieDetailsDuration.text = getString(R.string.movie_details_duration, runtime)
            }

            genres.forEach { genre ->
                Chip(
                    requireContext(),
                    null,
                    com.google.android.material.R.style.Widget_MaterialComponents_Chip_Entry
                )
                    .apply {
                        text = genre.name
                        chipBackgroundColor = ColorStateList.valueOf(
                            ContextCompat.getColor(requireContext(), R.color.colorPrimary)
                        )
                    }
                    .addToViewGroup(movieDetailsTags)
            }

            overview?.let {
                movieDetailsOverview.text = it
            }

            if (images.isNotEmpty()) {
                movieDetailsPhotosTitle.show()
                photosAdapter.submitList(images)
            }

            if (reviews.isNotEmpty()) {
                movieDetailsReviewsTitle.show()
                reviewsAdapter.submitList(reviews)
            } else {
                movieDetailsReviews.hide()
                movieDetailsReviewsTitle.hide()
            }
        }
    }
}