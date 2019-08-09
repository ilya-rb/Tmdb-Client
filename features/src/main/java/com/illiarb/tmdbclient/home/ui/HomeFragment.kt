package com.illiarb.tmdbclient.home.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.illiarb.tmdbclient.di.MoviesComponent
import com.illiarb.tmdbclient.home.R
import com.illiarb.tmdbclient.home.presentation.HomeModel
import com.illiarb.tmdbclient.home.presentation.HomeView
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.ext.awareOfWindowInsets
import com.illiarb.tmdbexplorer.coreui.ext.setVisible
import com.illiarb.core_ui_recycler_view.LayoutType
import com.illiarb.core_ui_recycler_view.RecyclerViewBuilder
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.domain.entity.MovieSection
import com.illiarb.tmdblcient.core.ext.emit
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.consumeAsFlow
import javax.inject.Inject

@FlowPreview
@InternalCoroutinesApi
class HomeFragment : BaseFragment(), Injectable, HomeView {

    @Inject
    lateinit var adapter: MovieAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        viewModelFactory.create(HomeModel::class.java)
    }

    private val movieClicks = Channel<Movie>()

    override val viewScope: CoroutineScope
        get() = viewLifecycleOwner.lifecycleScope

    override val movieSections: FlowCollector<List<MovieSection>> = emit { showMovieSections(it) }

    override val searchEnabled: FlowCollector<Boolean> = emit { homeSearch.setVisible(it) }

    override val accountEnabled: FlowCollector<Boolean> = emit { homeAccount.setVisible(it) }

    override fun onMovieClick(): Flow<Movie> = movieClicks.consumeAsFlow()

    override fun onAccountClick(): Flow<Unit> = homeAccount.clicks

    override fun onSearchClick(): Flow<Unit> = homeSearch.clicks

    override fun getContentView(): Int = R.layout.fragment_movies

    override fun inject(appProvider: AppProvider) = MoviesComponent.get(appProvider).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        viewModel.bind(this)
    }

    private fun showMovieSections(sections: List<MovieSection>) {
        adapter.items = sections
        adapter.notifyDataSetChanged()
    }
}