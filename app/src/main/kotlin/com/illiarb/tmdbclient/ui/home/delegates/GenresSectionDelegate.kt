package com.illiarb.tmdbclient.ui.home.delegates

import com.google.android.material.chip.Chip
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.illiarb.tmdbclient.databinding.ItemGenresSectionBinding
import com.illiarb.tmdbclient.libs.ui.common.OnClickListener
import com.illiarb.tmdbclient.services.tmdb.domain.Genre
import com.illiarb.tmdbclient.services.tmdb.domain.GenresSection
import com.illiarb.tmdbclient.services.tmdb.domain.MovieSection

fun genresSection(clickListener: OnClickListener<Genre>) =
  adapterDelegateViewBinding<GenresSection, MovieSection, ItemGenresSectionBinding>(
    { inflater, root -> ItemGenresSectionBinding.inflate(inflater, root, false) }
  ) {

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

        binding.genresChipGroup.addView(chip)
      }
    }
  }