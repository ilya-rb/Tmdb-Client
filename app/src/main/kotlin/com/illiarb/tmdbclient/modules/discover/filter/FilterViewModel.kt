package com.illiarb.tmdbclient.modules.discover.filter

import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbclient.libs.ui.base.viewmodel.BaseViewModel
import com.illiarb.tmdbclient.libs.ui.common.ViewStateEvent
import com.illiarb.tmdbclient.modules.discover.filter.FilterViewModel.State
import com.illiarb.tmdbclient.navigation.NavigationAction.Filters
import com.illiarb.tmdbclient.navigation.Router
import com.illiarb.tmdbclient.services.tmdb.domain.Filter
import com.illiarb.tmdbclient.services.tmdb.domain.Genre
import com.illiarb.tmdbclient.services.tmdb.domain.YearConstraints
import com.illiarb.tmdbclient.services.tmdb.interactor.FiltersInteractor
import com.illiarb.tmdbclient.services.tmdb.interactor.GenresInteractor
import kotlinx.coroutines.launch
import javax.inject.Inject

class FilterViewModel @Inject constructor(
  private val genresInteractor: GenresInteractor,
  private val router: Router,
  private val filtersInteractor: FiltersInteractor
) : BaseViewModel<State, FilterViewModel.Event>(initialState()) {

  companion object {
    fun initialState(): State = State(Filter.empty(), emptyList())
  }

  private val availableYearConstraints = YearConstraints.generateAvailableConstraints()

  init {
    viewModelScope.launch {
      val filter = filtersInteractor.getFilter()
      val genres = genresInteractor.getAllGenres()

      setState {
        if (filter.isError()) {
          copy(error = ViewStateEvent(filter.error().message ?: ""))
        } else {
          copy(
            filter = filter.unwrap(),
            availableGenres = if (genres.isError()) emptyList() else genres.unwrap()
          )
        }
      }
    }
  }

  override fun onUiEvent(event: Event) {
    super.onUiEvent(event)

    when (event) {
      is Event.YearConstraintSelected -> onYearConstraintSelected(event.position)
      is Event.YearFilterClicked -> setState {
        copy(selectYears = ViewStateEvent(availableYearConstraints))
      }
      is Event.GenreChecked -> onGenreChecked(event.id, event.isChecked)
      is Event.ClearFilter -> saveFilterAndExit(Filter.empty())
      is Event.ApplyFilter -> {
        setState {
          copy(filter = filter.copy(selectedGenreIds = event.selectedGenreIds))
        }
        saveFilterAndExit(currentState.filter)
      }
    }
  }

  private fun onYearConstraintSelected(position: Int) {
    setState {
      copy(
        filter = filter.copy(
          yearConstraints = availableYearConstraints[position]
        )
      )
    }
  }

  private fun onGenreChecked(genreId: Int, isChecked: Boolean) {
    setState {
      copy(
        filter = filter.copy(
          selectedGenreIds = if (filter.selectedGenreIds.contains(genreId) && !isChecked) {
            filter.selectedGenreIds.toMutableList().also {
              it.remove(genreId)
            }
          } else {
            filter.selectedGenreIds.toMutableList().also {
              it.add(genreId)
            }
          }
        )
      )
    }
  }

  private fun saveFilterAndExit(filter: Filter) {
    viewModelScope.launch {
      filtersInteractor.saveFilter(filter)
        .doIfOk {
          router.executeAction(Filters.BackToDiscover)
        }
        .doIfErr {
          setState {
            copy(error = ViewStateEvent(it.message ?: ""))
          }
        }
    }
  }

  sealed class Event {
    class YearConstraintSelected(val position: Int) : Event()
    class ApplyFilter(val selectedGenreIds: List<Int>) : Event()
    class GenreChecked(val id: Int, val isChecked: Boolean) : Event()
    object YearFilterClicked : Event()
    object ClearFilter : Event()
  }

  data class State(
    val filter: Filter,
    val availableGenres: List<Genre>,
    val selectYears: ViewStateEvent<List<YearConstraints>>? = null,
    val error: ViewStateEvent<String>? = null
  )
}