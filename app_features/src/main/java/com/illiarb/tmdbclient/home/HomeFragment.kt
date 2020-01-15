package com.illiarb.tmdbclient.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.illiarb.tmdbclient.home.HomeModel.UiEvent
import com.illiarb.tmdbclient.home.delegates.genresSectionDelegate
import com.illiarb.tmdbclient.home.delegates.movieSectionDelegate
import com.illiarb.tmdbclient.home.delegates.nowPlayingDelegate
import com.illiarb.tmdbclient.home.delegates.trendingSectionDelegate
import com.illiarb.tmdbclient.home.di.HomeComponent
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbclient.movies.home.databinding.FragmentMoviesBinding
import com.illiarb.tmdbexplorer.coreui.base.BaseViewBindingFragment
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.ext.doOnApplyWindowInsets
import com.illiarb.tmdbexplorer.coreui.ext.removeAdapterOnDetach
import com.illiarb.tmdbexplorer.coreui.ext.updatePadding
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.DelegatesAdapter
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.RecyclerViewStateSaver
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.util.Async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : BaseViewBindingFragment<FragmentMoviesBinding>(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: HomeModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory).get(DefaultHomeModel::class.java)
    }

    private val stateSaver = RecyclerViewStateSaver()
    private val adapter = DelegatesAdapter({
        listOf(
            movieSectionDelegate(it, stateSaver),
            nowPlayingDelegate(it),
            genresSectionDelegate(it),
            trendingSectionDelegate(it)
        )
    })

    override fun getViewBinding(inflater: LayoutInflater): FragmentMoviesBinding =
        FragmentMoviesBinding.inflate(inflater)

    override fun inject(appProvider: AppProvider) = HomeComponent.get(appProvider).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.appBar) {
            liftOnScrollTargetViewId = R.id.moviesList
            isLiftOnScroll = true
            doOnApplyWindowInsets { v, windowInsets, initialPadding ->
                v.updatePadding(top = initialPadding.top + windowInsets.systemWindowInsetTop)
            }
        }

        binding.moviesSettings.setOnClickListener {
            viewModel.onUiEvent(UiEvent.SettingsClick)
        }

        binding.moviesSwipeRefresh.isEnabled = false

        setupMoviesList()

        lifecycleScope.launch {
            adapter.clicks().collect {
                viewModel.onUiEvent(UiEvent.ItemClick(it))
            }
        }

        bind(viewModel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stateSaver.saveInstanceState()
    }

    private fun setupMoviesList() {
        binding.moviesList.apply {
            adapter = this@HomeFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                SpaceDecoration.edgeInnerSpaceVertical(
                    dimen(R.dimen.spacing_normal),
                    dimen(R.dimen.spacing_small)
                )
            )
            isNestedScrollingEnabled = false
            removeAdapterOnDetach()
            doOnApplyWindowInsets { v, insets, initialPadding ->
                v.updatePadding(bottom = initialPadding.bottom + insets.systemWindowInsetBottom)
            }
        }
    }

    private fun bind(viewModel: HomeModel) {
        viewModel.movieSections.observe(viewLifecycleOwner, Observer(::showMovieSections))
    }

    private fun showMovieSections(state: Async<List<MovieSection>>) {
        binding.moviesSwipeRefresh.isRefreshing = state is Async.Loading

        if (state is Async.Success) {
            adapter.submitList(state())
        }
    }
}