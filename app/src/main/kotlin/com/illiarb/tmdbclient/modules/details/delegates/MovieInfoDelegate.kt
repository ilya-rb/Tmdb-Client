package com.illiarb.tmdbclient.modules.details.delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.databinding.ItemMovieInfoBinding
import com.illiarb.tmdbclient.libs.ui.ext.setVisible
import com.illiarb.tmdbclient.libs.ui.widget.ExpandableTextView
import com.illiarb.tmdbclient.modules.details.MovieDetailsViewModel.MovieDetailsSection
import com.illiarb.tmdbclient.modules.details.MovieDetailsViewModel.MovieDetailsSection.MovieInfo

private const val OVERVIEW_TEXT_MAX_LINES = 3

fun movieInfoDelegate() =
  adapterDelegateViewBinding<MovieInfo, MovieDetailsSection, ItemMovieInfoBinding>(
    { inflater, root -> ItemMovieInfoBinding.inflate(inflater, root, false) }
  ) {

    bind {
      binding.movieDetailsTitle.text = item.movie.title
      binding.movieDetailsCountry.text = item.movie.country
      binding.movieDetailsDate.text = item.movie.releaseDate.date
      binding.movieDetailsTags.text = item.movie.getGenresString()
      binding.movieDetailsLength.text =
        getString(R.string.movie_details_duration, item.movie.runtime)

      binding.movieDetailsOverview.text = item.movie.overview
      binding.movieDetailsExpandOverview.setVisible(true)
      binding.movieDetailsOverview.post {
        val needExpansion = binding.movieDetailsOverview.setTextMaxLines(OVERVIEW_TEXT_MAX_LINES)
        binding.movieDetailsExpandOverview.setVisible(needExpansion)
      }

      binding.movieDetailsExpandOverview.setOnClickListener {
        binding.movieDetailsOverview.toggle()
      }

      binding.movieDetailsOverview.setExpandListener(object : ExpandableTextView.ExpandListener {
        override fun onCollapsed() {
          binding.movieDetailsExpandOverview.setText(R.string.movie_details_expand_overview)
        }

        override fun onExpanded() {
          binding.movieDetailsExpandOverview.setText(R.string.movie_details_collapse_overview)
        }
      })
    }
  }