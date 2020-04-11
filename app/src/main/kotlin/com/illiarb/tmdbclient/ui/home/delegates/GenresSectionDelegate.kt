package com.illiarb.tmdbclient.ui.home.delegates

import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbclient.libs.ui.common.OnClickListener
import com.illiarb.tmdbclient.services.tmdb.domain.Genre
import com.illiarb.tmdbclient.services.tmdb.domain.GenresSection
import com.illiarb.tmdbclient.services.tmdb.domain.MovieSection

fun genresSection(clickListener: OnClickListener<Genre>) =
  adapterDelegate<GenresSection, MovieSection>(R.layout.item_genres_section) {
    val chipGroup = itemView.findViewById<ChipGroup>(R.id.genresChipGroup)

    bind {
      item.genres.forEach { genre ->
        val chip = Chip(
          itemView.context,
          null,
          com.illiarb.tmdbclient.libs.ui.R.attr.materialChipAction
        )

        chip.text = genre.name
        chip.setOnClickListener {
          clickListener(genre)
        }

        chipGroup.addView(chip)
      }
    }
  }