package com.illiarb.tmdbclient.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.illiarb.core_ui_recycler_view.LayoutType
import com.illiarb.core_ui_recycler_view.RecyclerView
import com.illiarb.core_ui_recycler_view.RecyclerViewBuilder
import com.illiarb.tmdbclient.home.HomeViewModel.HomeUiEvent.ItemClick
import com.illiarb.tmdbclient.home.adapter.MovieAdapter
import com.illiarb.tmdbclient.home.di.HomeComponent
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbclient.movies.home.databinding.FragmentMoviesBinding
import com.illiarb.tmdbexplorer.coreui.base.BaseViewBindingFragment
import com.illiarb.tmdbexplorer.coreui.ext.awareOfWindowInsets
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.tools.Logger
import com.illiarb.tmdblcient.core.util.Async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : BaseViewBindingFragment<FragmentMoviesBinding>(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val adapter = MovieAdapter()

    private val viewModel: HomeViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory).get(HomeModel::class.java)
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentMoviesBinding =
        FragmentMoviesBinding.inflate(inflater)

    override fun inject(appProvider: AppProvider) = HomeComponent.get(appProvider).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.liftOnScrollTargetViewId = R.id.moviesList
        binding.appBar.isLiftOnScroll = true

        RecyclerViewBuilder
            .create {
                adapter(adapter)
                type(LayoutType.Linear())
                hasFixedSize(true)
                spaceBetween { spacingLeft = view.dimen(R.dimen.item_movie_spacing) }
            }
            .setupWith(binding.moviesList.recyclerView())

        view.awareOfWindowInsets()

        bind(viewModel)
    }

    private fun bind(viewModel: HomeViewModel) {
        viewModel.movieSections.observe(viewLifecycleOwner, Observer(::onMoviesStateChange))
        viewModel.isAccountVisible.observe(viewLifecycleOwner, Observer(::setAccountVisible))

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.clicks().collect {
                viewModel.onUiEvent(ItemClick(it))
            }
        }
    }

    private fun onMoviesStateChange(state: Async<List<MovieSection>>) {
        when (state) {
            is Async.Loading -> binding.moviesList.moveToState(RecyclerView.State.Loading)
            is Async.Fail -> Logger.e("Got error: ", state.error)
            is Async.Success -> {
                binding.moviesList.moveToState(RecyclerView.State.Content)
                adapter.items = state()
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun setAccountVisible(visible: Boolean) {
    }
}