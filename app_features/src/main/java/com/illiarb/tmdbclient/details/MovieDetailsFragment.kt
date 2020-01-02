package com.illiarb.tmdbclient.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.illiarb.core_ui_image.CropOptions
import com.illiarb.core_ui_image.RequestOptions.Companion.requestOptions
import com.illiarb.core_ui_image.loadImage
import com.illiarb.tmdbclient.details.MovieDetailsViewModel.DefaultDetailsViewModel
import com.illiarb.tmdbclient.details.di.MovieDetailsComponent
import com.illiarb.tmdbclient.details.photos.PhotosAdapter
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbclient.movies.home.databinding.FragmentMovieDetailsBinding
import com.illiarb.tmdbexplorer.coreui.base.BaseViewBindingFragment
import com.illiarb.tmdbexplorer.coreui.ext.awareOfWindowInsets
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.util.Async
import javax.inject.Inject

class MovieDetailsFragment : BaseViewBindingFragment<FragmentMovieDetailsBinding>(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

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

        val adapter = PhotosAdapter()

        binding.movieDetailsPhotos.let {
            it.adapter = adapter
            it.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            it.setHasFixedSize(true)
            it.addItemDecoration(
                SpaceDecoration(view.dimen(R.dimen.spacing_normal))
            )
        }

        bind(viewModel, adapter)

        ViewCompat.requestApplyInsets(view)
    }

    private fun bind(viewModel: MovieDetailsViewModel, adapter: PhotosAdapter) {
        viewModel.movie.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Async.Success -> showMovieDetails(it(), adapter)
            }
        })
    }

    private fun showMovieDetails(movie: Movie, adapter: PhotosAdapter) {
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

        adapter.items = movie.images
        adapter.notifyDataSetChanged()
    }
}