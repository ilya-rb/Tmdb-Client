package com.illiarb.tmdbclient.home.delegates

import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdblcient.core.domain.GenresSection
import com.illiarb.tmdblcient.core.domain.MovieSection

fun genresSectionDelegate(clickListener: OnClickListener) =
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