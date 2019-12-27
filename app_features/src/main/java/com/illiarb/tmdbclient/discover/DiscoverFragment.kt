package com.illiarb.tmdbclient.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.illiarb.core_ui_recycler_view.LayoutType
import com.illiarb.core_ui_recycler_view.RecyclerViewBuilder
import com.illiarb.tmdbclient.discover.di.DiscoverComponent
import com.illiarb.tmdbclient.movies.home.databinding.FragmentDiscoverBinding
import com.illiarb.tmdbexplorer.coreui.base.BaseViewBindingFragment
import com.illiarb.tmdbexplorer.coreui.ext.awareOfWindowInsets
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.domain.Genre
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class DiscoverFragment : BaseViewBindingFragment<FragmentDiscoverBinding>(), Injectable {

    companion object {
        private const val GRID_SPAN_COUNT = 3
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: DiscoverModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory).get(DefaultDiscoverModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DiscoverAdapter()

        RecyclerViewBuilder
            .create {
                type(LayoutType.Grid(GRID_SPAN_COUNT))
                adapter(adapter)
            }
            .setupWith(binding.discoverList)

        binding.discoverList.awareOfWindowInsets()

        ViewCompat.requestApplyInsets(view)

        bind(viewModel, adapter)
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentDiscoverBinding =
        FragmentDiscoverBinding.inflate(inflater)

    override fun inject(appProvider: AppProvider) = DiscoverComponent.get(appProvider).inject(this)

    private fun bind(viewModel: DiscoverModel, adapter: DiscoverAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.clicks().collect {
                viewModel.onUiEvent(DiscoverModel.UiEvent.ItemClick(it))
            }
        }

        viewModel.results.observe(viewLifecycleOwner, adapter)
        viewModel.genres.observe(viewLifecycleOwner, Observer { genres ->
            genres
                .map { genre ->
                    val chip = Chip(
                        requireContext(),
                        null,
                        com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice
                    )
                    bindChip(chip, genre)
                    chip
                }
                .forEach(binding.discoverGenres::addView)
        })
    }

    private fun bindChip(chip: Chip, genre: Genre) {
        chip.text = genre.name
        chip.id = genre.id
        chip.setOnClickListener {
            viewModel.onUiEvent(DiscoverModel.UiEvent.GenreSelected(chip.id))
        }
    }
}