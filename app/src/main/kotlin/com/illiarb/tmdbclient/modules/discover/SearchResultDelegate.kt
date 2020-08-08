package com.illiarb.tmdbclient.modules.discover

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.illiarb.tmdbclient.databinding.ItemSearchResultBinding
import com.illiarb.tmdbclient.libs.imageloader.CropOptions
import com.illiarb.tmdbclient.libs.ui.R
import com.illiarb.tmdbclient.libs.ui.common.OnClickListener
import com.illiarb.tmdbclient.libs.ui.ext.dimen
import com.illiarb.tmdbclient.services.tmdb.domain.Movie
import com.illiarb.tmdbclient.util.loadTmdbImage

fun searchResultDelegate(
  clickListener: OnClickListener<Movie>
) = adapterDelegateViewBinding<Movie, Any, ItemSearchResultBinding>(
  { inflater, root -> ItemSearchResultBinding.inflate(inflater, root, false) }
) {

  val cornerRadius = binding.root.dimen(R.dimen.corner_radius_normal)

  bind {
    binding.itemSearchResultTitle.text = item.title

    binding.itemSearchResultImage.loadTmdbImage(item.posterPath) {
      crop(CropOptions.CenterCrop)
      cornerRadius(cornerRadius)
    }

    binding.root.setOnClickListener {
      clickListener(item)
    }
  }
}