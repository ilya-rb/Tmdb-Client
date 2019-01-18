package com.illiarb.tmdbclient.feature.home.list.ui

import android.os.Bundle
import android.view.View
import com.illiarb.tmdbclient.feature.home.R
import com.illiarb.tmdbclient.feature.home.di.MoviesComponent
import com.illiarb.tmdbclient.feature.home.list.presentation.HomeModel
import com.illiarb.tmdbclient.feature.home.list.presentation.HomeUiState
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.common.ViewClickListener
import com.illiarb.tmdbexplorer.coreui.observable.LifecycleAwareObserver
import com.illiarb.tmdbexplorer.coreui.recyclerview.LayoutType
import com.illiarb.tmdbexplorer.coreui.recyclerview.RecyclerViewBuilder
import com.illiarb.tmdbexplorer.coreui.recyclerview.adapter.AdapterDelegate
import com.illiarb.tmdbexplorer.coreui.recyclerview.adapter.DelegateAdapter
import com.illiarb.tmdbexplorer.coreui.uiactions.ShowErrorDialogAction
import com.illiarb.tmdbexplorer.coreui.uiactions.UiAction
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.entity.Movie
import kotlinx.android.synthetic.main.fragment_movies.*
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class HomeFragment : BaseFragment<HomeModel>(), Injectable {

    @Inject
    lateinit var delegateAdapter: DelegateAdapter

    @Inject
    lateinit var delegatesSet: Set<@JvmSuppressWildcards AdapterDelegate>

    @Inject
    lateinit var viewClickListener: ViewClickListener

    private val stateObserver: LifecycleAwareObserver<HomeUiState> by lazy(NONE) {
        object : LifecycleAwareObserver<HomeUiState>(presentationModel.stateObservable()) {
            override fun onNewValue(state: HomeUiState) {
                render(state)
            }
        }
    }

    private val viewClickObserver: LifecycleAwareObserver<Any> by lazy(NONE) {
        object : LifecycleAwareObserver<Any>(viewClickListener.clickObservable()) {
            override fun onNewValue(state: Any) {
                when (state) {
                    is Movie -> presentationModel.onMovieClick(state)
                }
            }
        }
    }

    private val actionsObserver: LifecycleAwareObserver<UiAction> by lazy(NONE) {
        object: LifecycleAwareObserver<UiAction>(presentationModel.actionsObservable()) {
            override fun onNewValue(state: UiAction) {
                when (state) {
                    is ShowErrorDialogAction -> showErrorDialog(state.message)
                }
            }
        }
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
                    horizontally = resources.getDimensionPixelSize(R.dimen.item_movie_spacing)
                    addToFirst = true
                }
            }
            .setupWith(moviesList)

        homeSearch.setOnClickListener {
            presentationModel.onSearchClick()
        }

        homeAccount.setOnClickListener {
            presentationModel.onAccountClick()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        stateObserver.register(this)

        actionsObserver.register(this)

        viewClickObserver.register(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        moviesList.adapter = null
    }

    private fun render(state: HomeUiState) {
        movieProgress.visibility = if (state.isLoading) View.VISIBLE else View.GONE

        if (state.movies.isNotEmpty()) {
            delegateAdapter.submitList(state.movies)
        }
    }
}