package com.illiarb.tmdbclient.details

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.illiarb.coreuiimage.CropOptions
import com.illiarb.coreuiimage.RequestOptions.Companion.requestOptions
import com.illiarb.coreuiimage.loadImage
import com.illiarb.tmdbclient.details.MovieDetailsModel.UiEvent
import com.illiarb.tmdbclient.details.delegates.photoDelegate
import com.illiarb.tmdbclient.details.di.MovieDetailsComponent
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbclient.movies.home.databinding.FragmentMovieDetailsBinding
import com.illiarb.tmdbexplorer.coreui.base.BaseViewBindingFragment
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.ext.doOnApplyWindowInsets
import com.illiarb.tmdbexplorer.coreui.ext.removeAdapterOnDetach
import com.illiarb.tmdbexplorer.coreui.ext.updatePadding
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.DelegatesAdapter
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowMovieDetails
import com.illiarb.tmdblcient.core.util.Async
import javax.inject.Inject

class MovieDetailsFragment : BaseViewBindingFragment<FragmentMovieDetailsBinding>(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val adapter = DelegatesAdapter({ listOf(photoDelegate(it)) })

    private val viewModel: MovieDetailsModel by lazy(LazyThreadSafetyMode.NONE) {
        viewModelFactory.create(DefaultDetailsViewModel::class.java)
    }

    override fun inject(appProvider: AppProvider) {
        val id = requireArguments().getInt(ShowMovieDetails.EXTRA_MOVIE_DETAILS)
        MovieDetailsComponent.get(appProvider, id).inject(this)
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentMovieDetailsBinding =
        FragmentMovieDetailsBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()

        binding.swipeRefresh.isEnabled = false
        binding.movieDetailsPlay.setOnClickListener {
            viewModel.onUiEvent(UiEvent.PlayClicked)
        }

        setupPhotosList()

        ViewCompat.requestApplyInsets(view)

        bind(viewModel)
    }

    private fun setupToolbar() {
        binding.movieDetailsToolbar.apply {
            navigationIcon?.mutate()?.setTint(Color.WHITE)

            doOnApplyWindowInsets { v, windowInsets, initialPadding ->
                v.updatePadding(top = initialPadding.top + windowInsets.systemWindowInsetTop)
            }

            setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun setupPhotosList() {
        binding.movieDetailsPhotos.apply {
            adapter = this@MovieDetailsFragment.adapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            removeAdapterOnDetach()
            setHasFixedSize(true)
            addItemDecoration(
                SpaceDecoration(
                    orientation = LinearLayoutManager.HORIZONTAL,
                    spacingLeftFirst = dimen(R.dimen.spacing_normal),
                    spacingLeft = dimen(R.dimen.spacing_small),
                    spacingRight = dimen(R.dimen.spacing_small),
                    spacingRightLast = dimen(R.dimen.spacing_normal)
                )
            )
            doOnApplyWindowInsets { v, windowInsets, initialPadding ->
                v.updatePadding(bottom = initialPadding.bottom + windowInsets.systemWindowInsetBottom)
            }
        }
    }

    private fun bind(viewModel: MovieDetailsModel) {
        viewModel.movie.observe(viewLifecycleOwner, Observer {
            binding.swipeRefresh.isRefreshing = it is Async.Loading

            if (it is Async.Success) {
                showMovieDetails(it())
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
        adapter.submitList(movie.images)
    }
}