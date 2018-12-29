package com.illiarb.tmdbclient.feature.search.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.illiarb.tmdbclient.feature.search.R
import com.illiarb.tmdbclient.feature.search.SearchIntent
import com.illiarb.tmdbclient.feature.search.SearchView
import com.illiarb.tmdbclient.feature.search.SearchViewState
import com.illiarb.tmdbclient.feature.search.di.SearchComponent
import com.illiarb.tmdbclient.feature.search.viewmodel.SearchViewModel
import com.illiarb.tmdbclient.feature.search.viewmodel.ViewModelFactory
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.pipeline.MoviePipelineData
import com.illiarb.tmdbexplorer.coreui.pipeline.UiPipelineData
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.modules.search.SearchInteractor
import com.illiarb.tmdblcient.core.navigation.Router
import com.illiarb.tmdblcient.core.pipeline.EventPipeline
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_empty_view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author ilya-rb on 26.12.18.
 */
class SearchFragment : BaseFragment(), Injectable, SearchView {

    @Inject
    lateinit var searchAdapter: SearchAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var uiEventsPipeline: EventPipeline<@JvmSuppressWildcards UiPipelineData>

    @Inject
    lateinit var router: Router

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)
    }

    private val itemMovieClickSubject = PublishSubject.create<Movie>()

    override val intents: Observable<SearchIntent>
        get() = Observable.merge(
            createSearchTextObservable(),
            createItemMovieClickObservable()
        )

    override fun getContentView(): Int = R.layout.fragment_search

    override fun inject(appProvider: AppProvider) = SearchComponent.get(appProvider).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchResultsList.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        uiEventsPipeline.observeEvents()
            .ofType(MoviePipelineData::class.java)
            .map { it.movie }
            .subscribe { itemMovieClickSubject.onNext(it) }
            .addTo(destroyViewDisposable)

        viewModel.bind(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.unbind()
    }

    override val state: Consumer<SearchViewState>
        get() = Consumer { render(it) }

    override val sideEffects: Consumer<SearchInteractor.SideEffect>
        get() = Consumer { processSideEffects(it) }

    private fun render(state: SearchViewState) {
        searchProgress.visibility = if (state.isSearchRunning) View.VISIBLE else View.GONE

        val drawable =
            if (searchQuery.text.isNullOrEmpty()) {
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_search)
            } else {
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_clear_search)
            }
        searchIcon.setImageDrawable(drawable)

        searchAdapter.submitList(state.searchResults)

        if (state.searchResults.isEmpty()) {
            emptyView.visibility = View.VISIBLE
            searchResultsList.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            searchResultsList.visibility = View.VISIBLE
        }
    }

    private fun processSideEffects(sideEffect: SearchInteractor.SideEffect) =
        when (sideEffect) {
            is SearchInteractor.SideEffect.ShowError -> showErrorDialog(sideEffect.message)
            is SearchInteractor.SideEffect.ShowScreen -> router.navigateTo(sideEffect.screenData)
        }

    private fun createItemMovieClickObservable(): Observable<SearchIntent> =
        itemMovieClickSubject.map { SearchIntent.ShowMovieDetails(it.id) }

    private fun createSearchTextObservable(): Observable<SearchIntent> =
        Observable
            .create<String> { emitter ->
                val watcher = object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        s?.let {
                            if (!emitter.isDisposed) {
                                emitter.onNext(it.toString())
                            }
                        }
                    }
                }

                searchQuery.addTextChangedListener(watcher)

                emitter.setCancellable {
                    searchQuery.removeTextChangedListener(watcher)
                }
            }
            .skip(1)
            .debounce(400, TimeUnit.MILLISECONDS)
            .filter { it.isNotEmpty() }
            .map(SearchIntent::Search)
}