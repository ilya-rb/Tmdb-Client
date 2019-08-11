package com.illiarb.tmdbclient.movies.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.illiarb.core_ui_image.CropOptions
import com.illiarb.core_ui_image.ImageLoader
import com.illiarb.core_ui_image.RequestOptions
import com.illiarb.core_ui_recycler_view.LayoutOrientation
import com.illiarb.core_ui_recycler_view.LayoutType
import com.illiarb.core_ui_recycler_view.RecyclerViewBuilder
import com.illiarb.tmdbclient.movies.details.photos.PhotosAdapter
import com.illiarb.tmdbclient.di.MoviesComponent
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.ext.addToViewGroup
import com.illiarb.tmdbexplorer.coreui.ext.awareOfWindowInsets
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.domain.entity.Genre
import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.navigation.MovieDetailsScreen
import com.illiarb.tmdblcient.core.navigation.Screen
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MovieDetailsFragment : BaseFragment(), MovieDetailsView, Injectable {

    @Inject
    lateinit var photosAdapter: PhotosAdapter

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        viewModelFactory.create(MovieDetailsModel::class.java)
    }

    override val coroutineContext: CoroutineContext
        get() = lifecycleScope.coroutineContext

    override fun getContentView(): Int = R.layout.fragment_movie_details

    override fun inject(appProvider: AppProvider) = MoviesComponent.get(appProvider, requireActivity()).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieDetailsToolbar.apply {
            awareOfWindowInsets()
            movieDetailsToolbar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
        }

        ViewCompat.requestApplyInsets(view)

        arguments?.let {
            val id = it.getInt(MovieDetailsScreen.EXTRA_ID, 0)
            viewModel.getMovieDetails(id)
        }

        viewModel.bind(this)
    }

    override fun movie(movie: Movie) {
        with(movie) {
            movieDetailsTitle.text = title

            posterPath?.let {
                showMoviePoster(it)
            }

            movieDetailsRating.text = voteAverage.toString()

            if (runtime > 0) {
                movieDetailsDuration.text = getString(R.string.movie_details_duration, runtime)
            }

            if (movieDetailsTags.childCount == 0) {
                showMovieGenres(genres)
            }

            overview?.let {
                movieDetailsOverview.text = it
            }

            if (images.isNotEmpty()) {
                showMoviePhotos(images)
            }
        }
    }

    private fun findPhotosList(): RecyclerView? {
        return movieDetailsRoot.findViewById(R.id.movieDetailsPhotos)
    }

    private fun showMoviePoster(posterPath: String) {
        val options = RequestOptions.create {
            cornerRadius(resources.getDimensionPixelSize(R.dimen.corner_radius_normal))
            cropOptions(CropOptions.CENTER_CROP)
        }

        imageLoader.fromUrl(posterPath, movieDetailsPosterSmall, options)
        imageLoader.fromUrl(posterPath, movieDetailsPoster, options.copy(cornerRadius = 0))
    }

    private fun showMovieGenres(genres: List<Genre>) {
        genres.forEach { genre ->
            Chip(
                requireContext(),
                null,
                com.google.android.material.R.style.Widget_MaterialComponents_Chip_Entry
            )
                .apply {
                    text = genre.name
//                    chipBackgroundColor = ColorStateList.valueOf(
//                        ContextCompat.getColor(requireContext(),
//                    )
                }
                .addToViewGroup(movieDetailsTags)
        }
    }

    private fun showMoviePhotos(photos: List<String>) {
        // Check if we already inflated layout
        // because after rotation setState will be called again
        if (findPhotosList() != null) return

        // Using layout inflater because
        // seems like ViewStub doesn't support <merge> tag
        val photosView = LayoutInflater
            .from(requireContext())
            .inflate(
                R.layout.layout_photos_block,
                movieDetailsRoot,
                true
            )

        val photosList = photosView.findViewById<RecyclerView>(R.id.movieDetailsPhotos)

        RecyclerViewBuilder
            .create {
                adapter(photosAdapter)
                hasFixedSize(true)
                type(LayoutType.Linear())
                orientation(LayoutOrientation.HORIZONTAL)
                spaceBetween {
                    spacing = resources.getDimensionPixelSize(R.dimen.spacing_normal)
                    addToFirst = true
                    addToLast = true
                }
            }
            .setupWith(photosList)

        photosAdapter.items.clear()
        photosAdapter.items.addAll(photos)
        photosAdapter.notifyDataSetChanged()
    }
}