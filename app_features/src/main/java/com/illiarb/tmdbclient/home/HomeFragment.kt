package com.illiarb.tmdbclient.home

import android.animation.ArgbEvaluator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.illiarb.tmdbclient.home.HomeModel.UiEvent
import com.illiarb.tmdbclient.home.delegates.genresSectionDelegate
import com.illiarb.tmdbclient.home.delegates.movieSectionDelegate
import com.illiarb.tmdbclient.home.delegates.nowPlayingSectionDelegate
import com.illiarb.tmdbclient.home.delegates.trendingSectionDelegate
import com.illiarb.tmdbclient.home.di.HomeComponent
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbclient.movies.home.databinding.FragmentMoviesBinding
import com.illiarb.tmdbexplorer.coreui.base.BaseViewBindingFragment
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.ext.doOnApplyWindowInsets
import com.illiarb.tmdbexplorer.coreui.ext.getColorAttr
import com.illiarb.tmdbexplorer.coreui.ext.removeAdapterOnDetach
import com.illiarb.tmdbexplorer.coreui.ext.updatePadding
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.DelegatesAdapter
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.RecyclerViewStateSaver
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.util.Async
import javax.inject.Inject

class HomeFragment : BaseViewBindingFragment<FragmentMoviesBinding>(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: HomeModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory).get(DefaultHomeModel::class.java)
    }

    private val stateSaver = RecyclerViewStateSaver()
    private val clickListener: OnClickListener = { viewModel.onUiEvent(UiEvent.ItemClick(it)) }
    private val adapter = DelegatesAdapter(
        delegates = listOf(
            movieSectionDelegate(stateSaver, clickListener),
            nowPlayingSectionDelegate(stateSaver, clickListener),
            genresSectionDelegate(clickListener),
            trendingSectionDelegate(stateSaver, clickListener)
        )
    )

    override fun getViewBinding(inflater: LayoutInflater): FragmentMoviesBinding =
        FragmentMoviesBinding.inflate(inflater)

    override fun inject(appProvider: AppProvider) = HomeComponent.get(appProvider).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.doOnApplyWindowInsets { v, insets, padding ->
            v.updatePadding(top = padding.top + insets.systemWindowInsetTop)
        }

        binding.moviesSwipeRefresh.isEnabled = false
        binding.settings.setOnClickListener {
            viewModel.onUiEvent(UiEvent.SettingsClick)
        }

        setupAppBarScrollListener()
        setupMoviesList()

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
                SpaceDecoration(
                    spacingTopFirst = 0,
                    spacingTop = dimen(R.dimen.spacing_small),
                    spacingBottom = dimen(R.dimen.spacing_small),
                    spacingBottomLast = dimen(R.dimen.spacing_normal)
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

    @Suppress("MagicNumber")
    private fun setupAppBarScrollListener() {
        binding.moviesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            val colorEvaluator = ArgbEvaluator()
            val startColor = Color.TRANSPARENT
            val endColor: Int = requireView().getColorAttr(R.attr.colorPrimary)
            val endStateHeight: Int = requireView().dimen(R.dimen.app_bar_end_state_height)

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val scrollOffset = recyclerView.computeVerticalScrollOffset()
                val progress = calculateProgress(scrollOffset.coerceAtMost(endStateHeight), endStateHeight)
                val appBarColor = colorEvaluator.evaluate(progress.toFloat() / 100, startColor, endColor) as Int
                binding.appBar.setBackgroundColor(appBarColor)
            }

            private fun calculateProgress(current: Int, max: Int): Int {
                return current * 100 / max
            }
        })
    }

    private fun showMovieSections(state: Async<List<MovieSection>>) {
        binding.moviesSwipeRefresh.isRefreshing = state is Async.Loading

        if (state is Async.Success) {
            adapter.submitList(state())
        }
    }
}