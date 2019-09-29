package com.illiarb.tmdbclient.home

import android.os.Bundle
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
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.common.BackPressedHandler
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.util.Async
import com.illiarb.tmdblcient.core.util.Fail
import com.illiarb.tmdblcient.core.util.Loading
import com.illiarb.tmdblcient.core.util.Success
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : BaseFragment(R.layout.fragment_movies), Injectable, BackPressedHandler {

    @Inject
    lateinit var adapter: MovieAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var clickListener: OnClickListener

    private val viewModel: HomeViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory).get(HomeModel::class.java)
    }

    override fun inject(appProvider: AppProvider) =
        HomeComponent.get(appProvider, requireActivity()).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RecyclerViewBuilder
            .create {
                adapter(adapter)
                type(LayoutType.Linear())
                hasFixedSize(true)
                spaceBetween {
                    spacing = resources.getDimensionPixelSize(R.dimen.item_movie_spacing)
                }
            }
            .setupWith(moviesList.recyclerView())

        bind(viewModel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        moviesList.recyclerView().adapter = null
    }

    override fun handleBackPress(): Boolean {
        if (moviesList.recyclerView().scrollY > 0) {
            moviesList.recyclerView().smoothScrollToPosition(0)
            return true
        }
        return false
    }

    private fun bind(viewModel: HomeViewModel) {
        viewModel.movieSections.observe(viewLifecycleOwner, Observer(::onMoviesStateChange))
        viewModel.isAccountVisible.observe(viewLifecycleOwner, Observer(::setAccountVisible))

        viewLifecycleOwner.lifecycleScope.launch {
            clickListener.clicks().collect {
                viewModel.onUiEvent(ItemClick(it))
            }
        }
    }

    private fun onMoviesStateChange(state: Async<List<MovieSection>>) {
        when (state) {
            is Loading -> moviesList.moveToState(RecyclerView.State.Loading)
            is Fail -> moviesList.moveToState(RecyclerView.State.Error)
            is Success -> {
                moviesList.moveToState(RecyclerView.State.Content)
                adapter.items = state()
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun setAccountVisible(visible: Boolean) {
    }
}