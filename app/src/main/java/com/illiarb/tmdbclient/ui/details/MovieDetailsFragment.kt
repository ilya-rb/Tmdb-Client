package com.illiarb.tmdbclient.ui.details

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.illiarb.coreuiimage.CropOptions
import com.illiarb.coreuiimage.loadImage
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.FragmentMovieDetailsBinding
import com.illiarb.tmdbclient.di.AppComponent
import com.illiarb.tmdbclient.di.Injectable
import com.illiarb.tmdbclient.navigation.Router.Action.ShowMovieDetails
import com.illiarb.tmdbclient.tools.DateFormatter
import com.illiarb.tmdbclient.ui.details.MovieDetailsViewModel.Event
import com.illiarb.tmdbclient.ui.details.MovieDetailsViewModel.State
import com.illiarb.tmdbclient.ui.details.delegates.movieInfoDelegate
import com.illiarb.tmdbclient.ui.details.delegates.movieSimilarDelegate
import com.illiarb.tmdbclient.ui.details.delegates.photoSectionDelegate
import com.illiarb.tmdbclient.ui.details.di.DaggerMovieDetailsComponent
import com.illiarb.tmdbclient.util.Async
import com.illiarb.tmdbexplorer.coreui.base.BaseViewBindingFragment
import com.illiarb.tmdbexplorer.coreui.common.SnackbarController
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.ext.doOnApplyWindowInsets
import com.illiarb.tmdbexplorer.coreui.ext.removeAdapterOnDetach
import com.illiarb.tmdbexplorer.coreui.ext.setVisible
import com.illiarb.tmdbexplorer.coreui.ext.updatePadding
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.DelegatesAdapter
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.SpaceDecoration
import com.tmdbclient.servicetmdb.domain.Movie
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailsFragment : BaseViewBindingFragment<FragmentMovieDetailsBinding>(), Injectable {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  @Inject
  lateinit var dateFormatter: DateFormatter

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

  override fun inject(appComponent: AppComponent) =
    DaggerMovieDetailsComponent.builder()
      .dependencies(appComponent)
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

    viewLifecycleOwner.lifecycleScope.launch {
      SnackbarController().bind(binding.root, viewModel.errorState.map { it.message })
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
  }

  private fun showMovieDetails(movie: Movie) {
    binding.movieDetailsPoster.loadImage(movie.posterPath) {
      crop(CropOptions.CenterCrop)
    }

    binding.movieDetailsPlay.setVisible(!movie.videos.isNullOrEmpty())
    binding.movieDetailsPlay.setOnClickListener {
      viewModel.events.offer(Event.PlayClicked)
    }
  }
}