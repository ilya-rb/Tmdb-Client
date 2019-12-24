package com.illiarb.tmdbclient.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.illiarb.core_ui_image.CropOptions
import com.illiarb.core_ui_image.RequestOptions.Companion.requestOptions
import com.illiarb.core_ui_image.loadImage
import com.illiarb.core_ui_recycler_view.LayoutOrientation
import com.illiarb.core_ui_recycler_view.LayoutType
import com.illiarb.core_ui_recycler_view.RecyclerViewBuilder
import com.illiarb.tmdbclient.details.MovieDetailsViewModel.DefaultDetailsViewModel
import com.illiarb.tmdbclient.details.di.MovieDetailsComponent
import com.illiarb.tmdbclient.details.photos.PhotosAdapter
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbclient.movies.home.databinding.FragmentMovieDetailsBinding
import com.illiarb.tmdbexplorer.coreui.base.BaseViewBindingFragment
import com.illiarb.tmdbexplorer.coreui.ext.awareOfWindowInsets
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.util.Async
import javax.inject.Inject

class MovieDetailsFragment : BaseViewBindingFragment<FragmentMovieDetailsBinding>(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val photosAdapter = PhotosAdapter()

    private val viewModel: MovieDetailsViewModel by lazy(LazyThreadSafetyMode.NONE) {
        viewModelFactory.create(DefaultDetailsViewModel::class.java)
    }

    override fun inject(appProvider: AppProvider) {
        MovieDetailsComponent
            .get(
                appProvider,
                requireArguments().getInt(Router.Action.ShowMovieDetails.EXTRA_MOVIE_DETAILS)
            )
            .inject(this)
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentMovieDetailsBinding =
        FragmentMovieDetailsBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.movieDetailsToolbar.apply {
            awareOfWindowInsets()
            setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
        }

        RecyclerViewBuilder
            .create {
                adapter(photosAdapter)
                type(LayoutType.Linear())
                orientation(LayoutOrientation.HORIZONTAL)
                hasFixedSize(true)
                spaceBetween { spacingLeft = view.dimen(R.dimen.spacing_normal) }
            }
            .setupWith(binding.movieDetailsPhotos)

        view.awareOfWindowInsets()

        bind(viewModel)
    }

    private fun bind(viewModel: MovieDetailsViewModel) {
        viewModel.movie.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Async.Success -> showMovieDetails(it())
            }
        })
    }

    private fun showMovieDetails(movie: Movie) {
        with(binding) {
            movieDetailsTitle.text = movie.title
            movieDetailsOverview.text = movie.overview
            movieDetailsLength.text = getString(R.string.movie_details_duration, movie.runtime)
            movieDetailsYear.text = movie.releaseDate
            movieDetailsCountry.text = movie.country
            movieDetailsTags.text = movie.getGenresString()
            movieDetailsPoster.loadImage(movie.posterPath, requestOptions {
                cropOptions(CropOptions.CENTER_CROP)
            })
        }

        photosAdapter.items = movie.images
        photosAdapter.notifyDataSetChanged()
    }
}