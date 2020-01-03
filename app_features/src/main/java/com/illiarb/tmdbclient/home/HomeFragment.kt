package com.illiarb.tmdbclient.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.illiarb.tmdbclient.home.HomeModel.UiEvent
import com.illiarb.tmdbclient.home.adapter.MovieAdapter
import com.illiarb.tmdbclient.home.di.HomeComponent
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbclient.movies.home.databinding.FragmentMoviesBinding
import com.illiarb.tmdbexplorer.coreui.base.BaseViewBindingFragment
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.ext.doOnApplyWindowInsets
import com.illiarb.tmdbexplorer.coreui.ext.setVisible
import com.illiarb.tmdbexplorer.coreui.ext.updatePadding
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
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

        binding.moviesSwipeRefresh.isEnabled = false

        val adapter = MovieAdapter()

        binding.moviesList.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
            it.addItemDecoration(
                SpaceDecoration(
                    spacingTop = view.dimen(R.dimen.spacing_small),
                    spacingTopFirst = view.dimen(R.dimen.spacing_normal),
                    spacingBottom = view.dimen(R.dimen.spacing_small),
                    spacingBottomLast = view.dimen(R.dimen.spacing_normal)
                )
            )
        }

        bind(viewModel, adapter)
    }

    private fun bind(viewModel: HomeModel, adapter: MovieAdapter) {
        viewModel.isAccountVisible.observe(viewLifecycleOwner, Observer(::setAccountVisible))

        viewModel.movieSections.observe(viewLifecycleOwner, Observer { state ->
            binding.moviesSwipeRefresh.isRefreshing = state is Async.Loading

            if (state is Async.Success) {
                adapter.items = state()
                adapter.notifyDataSetChanged()
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.clicks().collect {
                viewModel.onUiEvent(UiEvent.ItemClick(it))
            }
        }
    }

    private fun setAccountVisible(visible: Boolean) = binding.moviesAccount.setVisible(visible)
}