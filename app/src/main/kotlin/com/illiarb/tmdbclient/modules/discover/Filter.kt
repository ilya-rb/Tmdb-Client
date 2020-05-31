package com.illiarb.tmdbclient.modules.discover

import java.util.Calendar

data class Filter(
  val selectedGenreIds: List<Int>,
  val yearConstraints: YearConstraints
) {

  companion object {

    fun empty(): Filter {
      return Filter(selectedGenreIds = emptyList(), yearConstraints = YearConstraints.AllYears)
    }
  }
}

sealed class YearConstraints {

  companion object {

    private const val SHOW_SINGLE_YEARS_COUNT = 3
    private const val SHOW_LAST_DECADES_COUNT = 3
    private const val YEARS_RANGE_STEP = 10

    fun generateAvailableConstraints(): List<YearConstraints> {
      val constraints = mutableListOf<YearConstraints>()
      val calendar = Calendar.getInstance()
      val currentYear = calendar.get(Calendar.YEAR)

      constraints.add(AllYears)

      for (i in 0 until SHOW_SINGLE_YEARS_COUNT) {
        constraints.add(
          SingleYear(year = currentYear - i)
        )
      }

      val startYear = currentYear - SHOW_SINGLE_YEARS_COUNT
      var step = 0
      for (i in 0 until SHOW_LAST_DECADES_COUNT) {
        constraints.add(
          YearRange(
            startYear = startYear - i - step,
            endYear = startYear - i - step - YEARS_RANGE_STEP
          )
        )
        step += YEARS_RANGE_STEP
      }

      return constraints
    }
  }

  object AllYears : YearConstraints()

  data class SingleYear(val year: Int) : YearConstraints()

  data class YearRange(val startYear: Int, val endYear: Int) : YearConstraints()

  override fun toString(): String {
    return when (this) {
      is AllYears -> "All years"
      is SingleYear -> year.toString()
      is YearRange -> "$startYear - $endYear"
    }
  }
}