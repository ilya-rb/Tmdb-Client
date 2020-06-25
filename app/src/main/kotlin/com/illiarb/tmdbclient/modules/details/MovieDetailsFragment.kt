package com.illiarb.tmdbclient.modules.details

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.FragmentMovieDetailsBinding
import com.illiarb.tmdbclient.di.AppProvider
import com.illiarb.tmdbclient.di.Injectable
import com.illiarb.tmdbclient.libs.imageloader.CropOptions
import com.illiarb.tmdbclient.libs.ui.base.BaseFragment
import com.illiarb.tmdbclient.libs.ui.common.SnackbarController
import com.illiarb.tmdbclient.libs.ui.ext.dimen
import com.illiarb.tmdbclient.libs.ui.ext.doOnApplyWindowInsets
import com.illiarb.tmdbclient.libs.ui.ext.removeAdapterOnDetach
import com.illiarb.tmdbclient.libs.ui.ext.setVisible
import com.illiarb.tmdbclient.libs.ui.ext.updatePadding
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.DelegatesAdapter
import com.illiarb.tmdbclient.libs.ui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdbclient.modules.details.MovieDetailsViewModel.Event
import com.illiarb.tmdbclient.modules.details.MovieDetailsViewModel.State
import com.illiarb.tmdbclient.modules.details.delegates.movieInfoDelegate
import com.illiarb.tmdbclient.modules.details.delegates.movieSimilarDelegate
import com.illiarb.tmdbclient.modules.details.delegates.photoSectionDelegate
import com.illiarb.tmdbclient.modules.details.di.DaggerMovieDetailsComponent
import com.illiarb.tmdbclient.navigation.NavigationAction
import com.illiarb.tmdbclient.services.tmdb.api.domain.Movie
import com.illiarb.tmdbclient.ui.loadTmdbImage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.illiarb.tmdbclient.libs.ui.R as UiR

class MovieDetailsFragment : BaseFragment(R.layout.fragment_movie_details), Injectable {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private val viewModel by viewModels<MovieDetailsViewModel>(factoryProducer = { viewModelFactory })
  private val viewBinding by viewBinding { fragment ->
    FragmentMovieDetailsBinding.bind(fragment.requireView())
  }

  private val snackbarController = SnackbarController()
  private val sectionsAdapter by lazy(LazyThreadSafetyMode.NONE) {
    DelegatesAdapter(
      movieInfoDelegate(),
      movieSimilarDelegate { viewModel.events.offer(Event.MovieClicked(it)) },
      photoSectionDelegate { /* TODO */ }
    )
  }

  override fun inject(appProvider: AppProvider) =
    DaggerMovieDetailsComponent.builder()
      .dependencies(appProvider)
      .movieId(requireArguments().getInt(NavigationAction.EXTRA_MOVIE_DETAILS_MOVIE_ID))
      .build()
      .inject(this)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewBinding.swipeRefresh.isEnabled = false

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
    viewBinding.movieDetailsRecycler.apply {
      adapter = sectionsAdapter
      layoutManager = LinearLayoutManager(context)
      isNestedScrollingEnabled = false
      removeAdapterOnDetach()
      addItemDecoration(
        SpaceDecoration(
          spacingTop = dimen(UiR.dimen.spacing_small),
          spacingBottom = dimen(UiR.dimen.spacing_small)
        )
      )
      doOnApplyWindowInsets { v, windowInsets, initialPadding ->
        v.updatePadding(bottom = initialPadding.top + windowInsets.systemWindowInsetBottom)
      }
    }
  }

  private fun setupToolbar() {
    viewBinding.movieDetailsToolbar.apply {
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
    viewBinding.swipeRefresh.isRefreshing = newState.isLoading

    sectionsAdapter.submitList(newState.movieSections)

    newState.movie?.let {
      showMovieDetails(it)
    }

    newState.error?.consume { message ->
      snackbarController.showMessage(viewBinding.root, message.message)
    }
  }

  private fun showMovieDetails(movie: Movie) {
    viewBinding.movieDetailsPoster.loadTmdbImage(movie.posterPath) {
      crop(CropOptions.CenterCrop)
    }

    viewBinding.movieDetailsPlay.setVisible(!movie.videos.isNullOrEmpty())
    viewBinding.movieDetailsPlay.setOnClickListener {
      viewModel.events.offer(Event.PlayClicked)
    }
  }
}