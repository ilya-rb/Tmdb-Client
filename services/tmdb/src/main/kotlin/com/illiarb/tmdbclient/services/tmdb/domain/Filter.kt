package com.illiarb.tmdbclient.services.tmdb.domain

import kotlinx.serialization.Serializable
import java.util.Calendar

@Serializable
data class Filter(
  val selectedGenreIds: List<Int>,
  val yearConstraints: YearConstraints
) {

  companion object {

    fun empty(): Filter {
      return Filter(selectedGenreIds = emptyList(), yearConstraints = YearConstraints.AllYears)
    }
  }

  fun count(): Int {
    var count = 0

    count += selectedGenreIds.size
    count += if (yearConstraints == YearConstraints.AllYears) 0 else 1

    return count
  }
}

@Serializable
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

  @Serializable
  object AllYears : YearConstraints()

  @Serializable
  data class SingleYear(val year: Int) : YearConstraints()

  @Serializable
  data class YearRange(val startYear: Int, val endYear: Int) : YearConstraints()

  fun displayName(): String {
    return when (this) {
      is AllYears -> "All years"
      is SingleYear -> year.toString()
      is YearRange -> "$startYear - $endYear"
    }
  }
}