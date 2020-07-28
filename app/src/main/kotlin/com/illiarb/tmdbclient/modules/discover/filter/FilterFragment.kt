package com.illiarb.tmdbclient.modules.discover.filter

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.chip.Chip
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.FragmentFilterBinding
import com.illiarb.tmdbclient.di.AppProvider
import com.illiarb.tmdbclient.di.Injectable
import com.illiarb.tmdbclient.libs.ui.base.BaseFragment
import com.illiarb.tmdbclient.libs.ui.common.SnackbarController
import com.illiarb.tmdbclient.libs.ui.ext.doOnApplyWindowInsets
import com.illiarb.tmdbclient.libs.ui.ext.getColorAttr
import com.illiarb.tmdbclient.libs.ui.ext.getTintedDrawable
import com.illiarb.tmdbclient.libs.ui.ext.tintMenuItemsWithColor
import com.illiarb.tmdbclient.libs.ui.ext.updatePadding
import com.illiarb.tmdbclient.modules.discover.filter.FilterViewModel.Event
import com.illiarb.tmdbclient.modules.discover.filter.FilterViewModel.State
import com.illiarb.tmdbclient.modules.discover.filter.di.DaggerFilterComponent
import com.illiarb.tmdbclient.services.tmdb.domain.Genre
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.android.material.R as MaterialR

class FilterFragment : BaseFragment(R.layout.fragment_filter), Injectable {

  companion object {
    private const val CHIP_REVEAL_DURATION = 200L
  }

  private val snackbarController = SnackbarController()
  private val chipRevealInterpolator = AccelerateDecelerateInterpolator()
  private val viewModel by viewModels<FilterViewModel>(factoryProducer = { viewModelFactory })
  private val viewBinding by viewBinding { fragment ->
    FragmentFilterBinding.bind(fragment.requireView())
  }

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  override fun inject(appProvider: AppProvider) {
    DaggerFilterComponent.factory()
      .create(dependencies = appProvider)
      .inject(this)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewBinding.filterYears.apply {
      setCompoundDrawablesRelativeWithIntrinsicBounds(
        null, // start
        null, // top
        view.context.getTintedDrawable(
          drawableRes = R.drawable.ic_arrow_drop_down,
          color = view.getColorAttr(MaterialR.attr.colorOnBackground)
        ), // end
        null // bottom
      )
      setOnClickListener {
        viewModel.events.offer(Event.YearFilterClicked)
      }
    }

    viewBinding.toolbar.menu
      .tintMenuItemsWithColor(viewBinding.root.getColorAttr(MaterialR.attr.colorOnPrimary))

    viewBinding.toolbar.setNavigationOnClickListener {
      activity?.onBackPressed()
    }

    viewBinding.toolbar.setOnMenuItemClickListener {
      when (it.itemId) {
        R.id.menu_filter_clear -> viewModel.events.offer(Event.ClearFilter)
        R.id.menu_filter_apply ->
          viewModel.events.offer(Event.ApplyFilter(viewBinding.filterGenres.checkedChipIds))
      }
      true
    }

    viewBinding.appBar.doOnApplyWindowInsets { v, insets, padding ->
      v.updatePadding(top = padding.top + insets.systemWindowInsetTop)
    }

    viewLifecycleOwner.lifecycleScope.launch {
      var oldState: State? = null

      viewModel.state.collect {
        render(oldState, it)
        oldState = it
      }
    }

    ViewCompat.requestApplyInsets(view)
  }

  private fun render(oldState: State?, newState: State) {
    viewBinding.filterYears.text = getString(
      R.string.filter_release_date,
      newState.filter.yearConstraints.displayName()
    )

    if (oldState?.availableGenres != newState.availableGenres) {
      if (viewBinding.filterGenres.childCount > 0) {
        viewBinding.filterGenres.removeAllViews()
      }

      newState.availableGenres.forEachIndexed { index, genre ->
        val isChecked = newState.filter.selectedGenreIds.contains(genre.id)
        val choiceChip = genre.asChoiceChip(isChecked)
        viewBinding.filterGenres.addView(choiceChip)
        revealChipAtPosition(choiceChip, index)
      }
    }

    if (newState.filter.selectedGenreIds.isEmpty()) {
      viewBinding.filterGenres.clearCheck()
    } else {
      newState.filter.selectedGenreIds.forEach {
        if (!viewBinding.filterGenres.checkedChipIds.contains(it)) {
          viewBinding.filterGenres.check(it)
        }
      }
    }

    newState.selectYears?.consume {
      val items = it.map { constraint -> constraint.displayName() }.toTypedArray()

      AlertDialog.Builder(requireContext())
        .setItems(items) { dialog, which ->
          viewModel.events.offer(Event.YearConstraintSelected(which))
          dialog.dismiss()
        }
        .create()
        .show()
    }

    newState.error?.consume {
      snackbarController.showMessage(viewBinding.root, it)
    }
  }

  private fun Genre.asChoiceChip(isChecked: Boolean): Chip {
    return Chip(
      context,
      null,
      com.illiarb.tmdbclient.libs.ui.R.attr.materialChipChoice
    ).also {
      it.text = name
      it.id = id
      it.isChecked = isChecked
      it.scaleX = 0f
      it.scaleY = 0f
      it.setOnCheckedChangeListener { _, isChecked ->
        viewModel.events.offer(Event.GenreChecked(id, isChecked))
      }
    }
  }

  @Suppress("MagicNumber")
  private fun revealChipAtPosition(chip: View, position: Int) {
    chip.animate()
      .scaleX(1f)
      .scaleY(1f)
      .setDuration(CHIP_REVEAL_DURATION)
      .setInterpolator(chipRevealInterpolator)
      .setStartDelay(20L * position)
      .start()
  }
}