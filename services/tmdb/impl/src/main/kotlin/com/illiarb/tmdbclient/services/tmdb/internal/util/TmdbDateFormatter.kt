package com.illiarb.tmdbclient.services.tmdb.internal.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

internal class TmdbDateFormatter @Inject constructor() {

  companion object {
    const val DISPLAY_DATE_FORMAT_PATTERN = "dd MMM yyyy"
    const val PARSE_DATE_FORMAT_PATTERN = "yyyy-mm-dd"
  }

  private val dateParser = SimpleDateFormat(PARSE_DATE_FORMAT_PATTERN, Locale.getDefault())
  private val dateFormatter = SimpleDateFormat(DISPLAY_DATE_FORMAT_PATTERN, Locale.getDefault())

  fun yearToReleaseDate(year: Int): String {
    val calendar = Calendar.getInstance().apply {
      set(Calendar.YEAR, year)
      set(Calendar.MONTH, Calendar.JANUARY)
      set(Calendar.DAY_OF_YEAR, 1)
    }
    return dateParser.format(Date(calendar.timeInMillis))
  }

  fun formatDate(date: String): String {
    return try {
      val parsed = dateParser.parse(date)
      if (parsed == null) {
        date
      } else {
        dateFormatter.format(parsed)
      }
    } catch (e: ParseException) {
      date
    }
  }
}