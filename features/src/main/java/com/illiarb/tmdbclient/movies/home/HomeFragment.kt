package com.illiarb.tmdbclient.movies.home

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.illiarb.core_ui_recycler_view.LayoutType
import com.illiarb.core_ui_recycler_view.RecyclerViewBuilder
import com.illiarb.tmdbclient.di.MoviesComponent
import com.illiarb.tmdbclient.movies.home.adapter.MovieAdapter
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.ext.awareOfWindowInsets
import com.illiarb.tmdbexplorer.coreui.ext.setVisible
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.domain.entity.MovieSection
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class HomeFragment : BaseFragment(), Injectable, HomeView {

    @Inject
    lateinit var adapter: MovieAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        lifecycleScope
        viewModelFactory.create(HomeModel::class.java)
    }

    private val uiEventsChannel = Channel<HomeView.UiEvent>()

    override val coroutineContext: CoroutineContext
        get() = lifecycleScope.coroutineContext

    override fun getContentView(): Int = R.layout.fragment_movies

    override fun inject(appProvider: AppProvider) = MoviesComponent.get(appProvider, requireActivity()).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.onClickBlock = {
            lifecycleScope.launch {
                uiEventsChannel.send(HomeView.UiEvent.MovieClick(it))
            }
        }

        RecyclerViewBuilder
            .create {
                adapter(adapter)
                type(LayoutType.Linear())
                hasFixedSize(true)
                spaceBetween { spacing = resources.getDimensionPixelSize(R.dimen.item_movie_spacing) }
            }
            .setupWith(moviesList)

        topView.awareOfWindowInsets()

        ViewCompat.requestApplyInsets(view)

        homeAccount.setOnClickListener {
            lifecycleScope.launch {
                uiEventsChannel.send(HomeView.UiEvent.AccountClick)
            }
        }

        viewModel.bind(this)
    }

    override val uiEvents: Flow<HomeView.UiEvent>
        get() = uiEventsChannel.consumeAsFlow()

    override fun movieSections(sections: List<MovieSection>) {
        adapter.items = sections
        adapter.notifyDataSetChanged()
    }

    override fun progressVisible(visible: Boolean) = movieProgress.setVisible(visible)

    override fun accountEnabled(enabled: Boolean) = homeAccount.setVisible(enabled)
}