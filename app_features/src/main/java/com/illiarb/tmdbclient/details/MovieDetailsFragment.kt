package com.illiarb.tmdbclient.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
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
import com.illiarb.tmdbclient.details.di.MovieDetailsComponent
import com.illiarb.tmdbclient.details.photos.PhotosAdapter
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdbexplorer.coreui.ext.awareOfWindowInsets
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.util.Success
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailsFragment : BaseFragment(R.layout.fragment_movie_details), Injectable {

    @Inject
    lateinit var photosAdapter: PhotosAdapter

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var onClickListener: OnClickListener

    private val viewModel: MovieDetailsViewModel by lazy(LazyThreadSafetyMode.NONE) {
        viewModelFactory.create(MovieDetailsModel::class.java)
    }

    override fun inject(appProvider: AppProvider) {
        MovieDetailsComponent
            .get(
                appProvider,
                requireActivity(),
                arguments!!.getInt(Router.Action.ShowMovieDetails.EXTRA_MOVIE_DETAILS)
            )
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieDetailsToolbar.apply {
            awareOfWindowInsets()
            movieDetailsToolbar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
        }

        ViewCompat.requestApplyInsets(view)

        bind(viewModel)
    }

    private fun bind(viewModel: MovieDetailsViewModel) {
        viewModel.movie.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> showMovieDetails(it())
            }
        })
    }

    private fun showMovieDetails(movie: Movie) = with(movie) {
        movieDetailsTitle.text = title
        movieDetailsOverview.text = overview
        movieDetailsLength.text = getString(R.string.movie_details_duration, runtime)
        movieDetailsYear.text = releaseDate
        movieDetailsCountry.text = country

        posterPath?.let {
            imageLoader.fromUrl(posterPath, movieDetailsPoster, RequestOptions.create {
                cropOptions(CropOptions.CENTER_CROP)
            })
        }

        if (genres.isNotEmpty()) {
            val builder = StringBuilder()

            genres.forEachIndexed { index, genre ->
                builder.append(genre.name)

                if (index < genres.size - 1) {
                    builder.append(", ")
                }
            }

            movieDetailsTags.text = builder
        }

        if (images.isNotEmpty()) {
            showMoviePhotos(images)
        }
    }

    private fun showMoviePhotos(photos: List<String>) {
        // Using layout inflater because
        // seems like ViewStub doesn't support <merge> tag
//        val photosView = LayoutInflater
//            .from(requireContext())
//            .inflate(
//                R.layout.layout_photos_block,
//                movieDetailsRoot,
//                true
//            )
//
//        val photosList = photosView.findViewById<RecyclerView>(R.id.movieDetailsPhotos)
//
//        RecyclerViewBuilder
//            .create {
//                adapter(photosAdapter)
//                hasFixedSize(true)
//                type(LayoutType.Linear())
//                orientation(LayoutOrientation.HORIZONTAL)
//                spaceBetween {
//                    spacing = resources.getDimensionPixelSize(R.dimen.spacing_normal)
//                    addToFirst = true
//                    addToLast = true
//                }
//            }
//            .setupWith(photosList)
//
//        photosAdapter.items = photos
//        photosAdapter.notifyDataSetChanged()
    }
}