package com.illiarb.tmdbclient.modules.details

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.illiarb.tmdbclient.databinding.FragmentMovieDetailsBinding
import com.illiarb.tmdbclient.di.AppProvider
import com.illiarb.tmdbclient.di.Injectable
import com.illiarb.tmdbclient.libs.imageloader.CropOptions
import com.illiarb.tmdbclient.libs.tools.DateFormatter
import com.illiarb.tmdbclient.libs.ui.R
import com.illiarb.tmdbclient.libs.ui.base.BaseViewBindingFragment
import com.illiarb.tmdbclient.libs.ui.common.SnackbarController
import com.illiarb.tmdbclient.libs.ui.ext.dimen
import com.illiarb.tmdbclient.libs.ui.ext.doOnApplyWindowInsets
import com.illiarb.tmdbclient.libs.ui.ext.removeAdapterOnDetach
import com.illiarb.tmdbclient.libs.ui.ext.setVisible
import com.illiarb.tmdbclient.libs.ui.ext.updatePadding
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.DelegatesAdapter
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdbclient.libs.util.Async
import com.illiarb.tmdbclient.navigation.Action.ShowMovieDetails
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.modules.details.MovieDetailsViewModel.Event
import com.illiarb.tmdbclient.modules.details.MovieDetailsViewModel.State
import com.illiarb.tmdbclient.modules.details.delegates.movieInfoDelegate
import com.illiarb.tmdbclient.modules.details.delegates.movieSimilarDelegate
import com.illiarb.tmdbclient.modules.details.delegates.photoSectionDelegate
import com.illiarb.tmdbclient.modules.details.di.DaggerMovieDetailsComponent
import com.illiarb.tmdbclient.ui.loadTmdbImage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailsFragment : BaseViewBindingFragment<FragmentMovieDetailsBinding>(), Injectable {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  @Inject
  lateinit var dateFormatter: DateFormatter

  private val snackbarController = SnackbarController()
  private val sectionsAdapter by lazy(LazyThreadSafetyMode.NONE) {
    DelegatesAdapter(
      movieInfoDelegate(dateFormatter),
      movieSimilarDelegate { viewModel.events.offer(Event.MovieClicked(it)) },
      photoSectionDelegate { }
    )
  }

  private val viewModel: MovieDetailsViewModel by lazy(LazyThreadSafetyMode.NONE) {
    viewModelFactory.create(MovieDetailsViewModel::class.java)
  }

  override fun inject(appProvider: AppProvider) =
    DaggerMovieDetailsComponent.builder()
      .dependencies(appProvider)
      .movieId(requireArguments().getInt(ShowMovieDetails.EXTRA_MOVIE_DETAILS))
      .build()
      .inject(this)

  override fun getViewBinding(inflater: LayoutInflater): FragmentMovieDetailsBinding =
    FragmentMovieDetailsBinding.inflate(inflater)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.swipeRefresh.isEnabled = false

    setupToolbar()
    setupSectionsList()

    ViewCompat.requestApplyInsets(view)

    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.state.collect {
        render(it)
      }
    }
  }

  private fun setupSectionsList() {
    binding.movieDetailsRecycler.apply {
      adapter = sectionsAdapter
      layoutManager = LinearLayoutManager(context)
      isNestedScrollingEnabled = false
      removeAdapterOnDetach()
      addItemDecoration(
        SpaceDecoration(
          spacingTop = dimen(R.dimen.spacing_small),
          spacingBottom = dimen(R.dimen.spacing_small)
        )
      )
    }
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

  private fun render(newState: State) {
    binding.swipeRefresh.isRefreshing = newState.movie is Async.Loading<*>

    sectionsAdapter.submitList(newState.movieSections)

    newState.movie.doOnSuccess {
      showMovieDetails(it)
    }

    newState.error?.let { error ->
      error.consume { message ->
        snackbarController.showMessage(binding.root, message.message)
      }
    }
  }

  private fun showMovieDetails(movie: Movie) {
    binding.movieDetailsPoster.loadTmdbImage(movie.posterPath) {
      crop(CropOptions.CenterCrop)
    }

    binding.movieDetailsPlay.setVisible(!movie.videos.isNullOrEmpty())
    binding.movieDetailsPlay.setOnClickListener {
      viewModel.events.offer(Event.PlayClicked)
    }
  }
}