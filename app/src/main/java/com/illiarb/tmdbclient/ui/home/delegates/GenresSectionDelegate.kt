package com.illiarb.tmdbclient.ui.home.delegates

import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.tmdbclient.servicetmdb.domain.Genre
import com.tmdbclient.servicetmdb.domain.GenresSection
import com.tmdbclient.servicetmdb.domain.MovieSection

fun genresSection(clickListener: OnClickListener<Genre>) =
  adapterDelegate<GenresSection, MovieSection>(R.layout.item_genres_section) {
    val chipGroup = itemView.findViewById<ChipGroup>(R.id.genresChipGroup)

    bind {
      item.genres.forEach { genre ->
        val chip = Chip(
          itemView.context,
          null,
          com.illiarb.tmdbexplorer.coreui.R.attr.materialChipAction
        )

        chip.text = genre.name
        chip.setOnClickListener {
          clickListener(genre)
        }

        chipGroup.addView(chip)
      }
    }
  }