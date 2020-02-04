package com.illiarb.tmdbclient.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.illiarb.tmdbclient.common.delegates.movieDelegate
import com.illiarb.tmdbclient.discover.DiscoverModel.UiEvent
import com.illiarb.tmdbclient.discover.di.DiscoverComponent
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbclient.movies.home.databinding.FragmentDiscoverBinding
import com.illiarb.tmdbexplorer.coreui.base.BaseViewBindingFragment
import com.illiarb.tmdbexplorer.coreui.common.SizeSpec
import com.illiarb.tmdbexplorer.coreui.common.Text
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.ext.removeAdapterOnDetach
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.DelegatesAdapter
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowDiscover
import javax.inject.Inject

class DiscoverFragment : BaseViewBindingFragment<FragmentDiscoverBinding>(), Injectable {

    companion object {
        private const val GRID_SPAN_COUNT = 3
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var discoverGenres: ChipGroup
    private lateinit var filtersContainer: ViewGroup

    private val adapter = DelegatesAdapter(
        movieDelegate(
            SizeSpec.MatchParent,
            SizeSpec.Fixed(R.dimen.discover_item_movie_height)
        ) {
            viewModel.onUiEvent(UiEvent.ItemClick(it))
        }
    )

    private val viewModel: DiscoverModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory).get(DefaultDiscoverModel::class.java)
    }

    override fun inject(appProvider: AppProvider) {
        val id = arguments?.getInt(ShowDiscover.EXTRA_GENRE_ID, Genre.GENRE_ALL) ?: Genre.GENRE_ALL
        DiscoverComponent.get(appProvider, id).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // filters layout added via <include> tag and not supported by view binding
        discoverGenres = binding.root.findViewById(R.id.discoverGenres)
        filtersContainer = binding.root.findViewById(R.id.discoverFiltersContainer)

        binding.discoverSwipeRefresh.isEnabled = false

        setupToolbar()
        setupFilters()
        setupDiscoverList()

        ViewCompat.requestApplyInsets(view)

        bind(viewModel)
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentDiscoverBinding =
        FragmentDiscoverBinding.inflate(inflater)

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun setupFilters() {
        binding.root.findViewById<View>(R.id.discoverApplyFilter).setOnClickListener {
            dismissFiltersPanel()
            viewModel.onUiEvent(UiEvent.ApplyFilter(discoverGenres.checkedChipId))
        }

        binding.root.findViewById<View>(R.id.discoverClearFilter).setOnClickListener {
            discoverGenres.clearCheck()
            dismissFiltersPanel()
            viewModel.onUiEvent(UiEvent.ClearFilter)
        }

        binding.discoverRoot.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) = Unit
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) = Unit
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) = Unit
            override fun onTransitionCompleted(root: MotionLayout?, state: Int) {
                binding.discoverOverlay.isClickable = state == R.id.filtersEnd
            }
        })
    }

    private fun setupDiscoverList() {
        binding.discoverList.apply {
            adapter = this@DiscoverFragment.adapter
            layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
            removeAdapterOnDetach()

            val spacing = dimen(R.dimen.spacing_small)
            addItemDecoration(SpaceDecoration(spacing, spacing, spacing, spacing))
        }
    }

    private fun bind(viewModel: DiscoverModel) {
        viewModel.results.observe(viewLifecycleOwner, adapter)
        viewModel.genres.observe(viewLifecycleOwner, discoverGenres.genres())
        viewModel.selectedChip.observe(viewLifecycleOwner, Observer(::selectChip))
        viewModel.isLoading.observe(viewLifecycleOwner, Observer(::showLoading))
        viewModel.screenTitle.observe(viewLifecycleOwner, Observer(::setScreenTitle))
    }

    private fun dismissFiltersPanel() {
        binding.discoverRoot.transitionToStart()
    }

    private fun selectChip(id: Int) {
        if (discoverGenres.checkedChipId != id) {
            discoverGenres.check(id)
        }
    }

    private fun setScreenTitle(title: Text) {
        binding.toolbar.title = when (title) {
            is Text.AsString -> title.text
            is Text.AsResource -> getString(title.id)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.discoverSwipeRefresh.isRefreshing = isLoading
    }

    private fun ChipGroup.genres(): Observer<List<Genre>> = Observer { genres ->
        if (discoverGenres.childCount == 0) {
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
}