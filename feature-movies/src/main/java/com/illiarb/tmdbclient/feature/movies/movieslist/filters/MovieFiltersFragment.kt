package com.illiarb.tmdbclient.feature.movies.movieslist.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbclient.feature.movies.di.MoviesComponent
import com.illiarb.tmdbexplorer.coreui.state.UiState
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.system.EventBus
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_movie_filters.*
import javax.inject.Inject

/**
 * @author ilya-rb on 26.10.18.
 */
class MovieFiltersFragment : BottomSheetDialogFragment(), Injectable {

    companion object {

        fun show(fragmentManager: FragmentManager) {
            MovieFiltersFragment().show(fragmentManager, MovieFiltersFragment::class.java.name)
        }
    }

    @Inject
    lateinit var bus: EventBus

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val destroyDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_movie_filters, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModelFactory.create(MovieFiltersViewModel::class.java)
            .also {
                it.observeMovieFilters()
                    .subscribe(::onFiltersStateChanged, Throwable::printStackTrace)
                    .addTo(destroyDisposable)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        destroyDisposable.clear()
    }

    private fun onFiltersStateChanged(uiState: UiState<List<MovieFilter>>) {
        if (uiState.hasData()) {
            uiState.requireData().forEach { filter ->
                TextView(requireContext(), null, 0, R.style.MovieFilter)
                    .apply {
                        text = filter.name
                        setOnClickListener {
                            bus.postEvent(filter)
                            this@MovieFiltersFragment.dismiss()
                        }
                    }
                    .also { filtersContainer.addView(it) }
            }
        }
    }

    override fun inject(appProvider: AppProvider) = MoviesComponent.get(appProvider, requireActivity()).inject(this)
}