package com.illiarb.tmdbclient.feature.home.list.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import com.illiarb.tmdbclient.feature.home.R
import com.illiarb.tmdbclient.feature.home.di.MoviesComponent
import com.illiarb.tmdbclient.feature.home.list.presentation.HomeModel
import com.illiarb.tmdbclient.feature.home.list.presentation.HomeUiState
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.common.ViewClickListener
import com.illiarb.tmdbexplorer.coreui.ext.awareOfWindowInsets
import com.illiarb.tmdbexplorer.coreui.ext.setVisible
import com.illiarb.tmdbexplorer.coreui.recyclerview.LayoutType
import com.illiarb.tmdbexplorer.coreui.recyclerview.RecyclerViewBuilder
import com.illiarb.tmdbexplorer.coreui.recyclerview.adapter.AdapterDelegate
import com.illiarb.tmdbexplorer.coreui.recyclerview.adapter.DelegateAdapter
import com.illiarb.tmdbexplorer.coreui.util.LawObserver
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.domain.entity.Movie
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

@ExperimentalCoroutinesApi
class HomeFragment : BaseFragment<HomeModel>(), Injectable {

    @Inject
    lateinit var delegateAdapter: DelegateAdapter

    @Inject
    lateinit var delegatesSet: Set<@JvmSuppressWildcards AdapterDelegate>

    @Inject
    lateinit var viewClickListener: ViewClickListener

    private val stateObserver: LawObserver<HomeUiState> by lazy(NONE) {
        LawObserver.create(presentationModel.stateObservable(), ::render)
    }

    private val viewClickObserver: LawObserver<Any> by lazy(NONE) {
        LawObserver.create(viewClickListener.clickObservable(), ::onClick)
    }

    override fun getContentView(): Int = R.layout.fragment_movies

    override fun inject(appProvider: AppProvider) = MoviesComponent.get(appProvider).inject(this)

    override fun getModelClass(): Class<HomeModel> = HomeModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RecyclerViewBuilder
            .create {
                adapter(delegateAdapter.also { it.addDelegatesFromSet(delegatesSet) })
                type(LayoutType.Linear())
                hasFixedSize(true)
                spaceBetween {
                    spacing = resources.getDimensionPixelSize(R.dimen.item_movie_spacing)
                }
            }
            .setupWith(moviesList)

        topView.awareOfWindowInsets()

        homeSearch.setOnClickListener {
            presentationModel.onSearchClick()
        }

        homeAccount.setOnClickListener {
            presentationModel.onAccountClick()
        }

        ViewCompat.requestApplyInsets(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        stateObserver.register(this)

        viewClickObserver.register(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        moviesList.adapter = null
    }

    private fun onClick(item: Any) {
        when (item) {
            is Movie -> presentationModel.onMovieClick(item)
        }
    }

    private fun render(state: HomeUiState) = runOnUi {
        homeAccount.setVisible(state.isAuthEnabled)
        homeSearch.setVisible(state.isSearchEnabled)

        movieProgress.setVisible(state.isLoading)

        if (state.movies.isNotEmpty()) {
            delegateAdapter.submitList(state.movies)
        }
    }
}