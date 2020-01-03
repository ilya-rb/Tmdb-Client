package com.illiarb.tmdbclient.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.illiarb.tmdbclient.discover.DiscoverModel.UiEvent
import com.illiarb.tmdbclient.discover.di.DiscoverComponent
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbclient.movies.home.databinding.FragmentDiscoverBinding
import com.illiarb.tmdbexplorer.coreui.base.BaseViewBindingFragment
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.ext.doOnApplyWindowInsets
import com.illiarb.tmdbexplorer.coreui.ext.updatePadding
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowDiscover
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class DiscoverFragment : BaseViewBindingFragment<FragmentDiscoverBinding>(), Injectable {

    companion object {
        private const val GRID_SPAN_COUNT = 3
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var discoverGenres: ChipGroup

    private val viewModel: DiscoverModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory).get(DefaultDiscoverModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val genreId = it.getInt(ShowDiscover.EXTRA_GENRE_ID, Genre.GENRE_ALL)
            viewModel.onUiEvent(UiEvent.Init(genreId))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // filters layout added via <include> tag and not supported by view binding
        discoverGenres = binding.root.findViewById(R.id.discoverGenres)

        val adapter = DiscoverAdapter()

        binding.discoverList.let {
            it.adapter = adapter
            it.layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)

            val spacing = view.dimen(R.dimen.spacing_small)
            it.addItemDecoration(
                SpaceDecoration(spacing, spacing, spacing, spacing)
            )
        }

        binding.toolbar.doOnApplyWindowInsets { v, windowInsets, initialPadding ->
            v.updatePadding(top = initialPadding.top + windowInsets.systemWindowInsetTop)
        }

        binding.root.findViewById<View>(R.id.discoverApplyFilter).setOnClickListener {
            dismissFiltersPanel()
            viewModel.onUiEvent(UiEvent.ApplyFilter(discoverGenres.checkedChipId))
        }

        binding.root.findViewById<View>(R.id.discoverClearFilter).setOnClickListener {
            discoverGenres.clearCheck()
            dismissFiltersPanel()
            viewModel.onUiEvent(UiEvent.ClearFilter)
        }

        ViewCompat.requestApplyInsets(view)

        bind(viewModel, adapter)
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentDiscoverBinding =
        FragmentDiscoverBinding.inflate(inflater)

    override fun inject(appProvider: AppProvider) = DiscoverComponent.get(appProvider).inject(this)

    private fun bind(viewModel: DiscoverModel, adapter: DiscoverAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.clicks().collect {
                viewModel.onUiEvent(UiEvent.ItemClick(it))
            }
        }

        viewModel.results.observe(viewLifecycleOwner, adapter)
        viewModel.genres.observe(viewLifecycleOwner, discoverGenres.genres())
    }

    private fun dismissFiltersPanel() {
        binding.discoverRoot.transitionToStart()
    }

    private fun ChipGroup.genres(): Observer<List<Genre>> = Observer { genres ->
        removeAllViews()

        genres.forEach { genre ->
            val chip = Chip(
                context,
                null,
                com.illiarb.tmdbexplorer.coreui.R.attr.materialChipChoice
            )
            chip.text = genre.name
            chip.id = genre.id
            addView(chip)
        }
    }
}