package com.illiarb.tmdbclient.modules.details.v2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.di.AppProvider
import com.illiarb.tmdbclient.di.Injectable
import com.illiarb.tmdbclient.libs.ui.base.BaseFragment
import com.illiarb.tmdbclient.libs.ui.v2.theme.TmdbTheme
import com.illiarb.tmdbclient.modules.details.MovieDetailsViewModel
import com.illiarb.tmdbclient.modules.details.di.DaggerMovieDetailsComponent
import com.illiarb.tmdbclient.modules.details.v2.util.ProvideDisplayInsets
import com.illiarb.tmdbclient.navigation.NavigationAction
import javax.inject.Inject

class MovieDetailsFragment : BaseFragment(R.layout.fragment_movie_details_compose), Injectable {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private val viewModel by viewModels<MovieDetailsViewModel>(factoryProducer = { viewModelFactory })

  override fun inject(appProvider: AppProvider) {
    DaggerMovieDetailsComponent.factory()
      .create(
        movieId = requireArguments().getInt(NavigationAction.EXTRA_MOVIE_DETAILS_MOVIE_ID),
        dependencies = appProvider
      )
      .inject(this)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return super.onCreateView(inflater, container, savedInstanceState).apply {
      this?.findViewById<ComposeView>(R.id.compose_view)?.setContent {
        TmdbTheme(context = context) {
          ProvideDisplayInsets {
            MovieDetails(
              context = viewLifecycleOwner.lifecycleScope.coroutineContext,
              state = viewModel.state,
              eventsSender = viewModel.events,
            )
          }
        }
      }
    }
  }
}