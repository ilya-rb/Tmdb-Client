package com.illiarb.tmdbclient.ui.details.delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.illiarb.tmdbclient.databinding.ItemPhotoBinding
import com.illiarb.tmdbclient.libs.imageloader.CropOptions
import com.illiarb.tmdbclient.libs.imageloader.loadImage
import com.illiarb.tmdbclient.libs.ui.R
import com.illiarb.tmdbclient.libs.ui.common.OnClickListener
import com.illiarb.tmdbclient.services.tmdb.domain.Image

fun photoDelegate(
  clickListener: OnClickListener<Image>
) = adapterDelegateViewBinding<Image, Image, ItemPhotoBinding>(
  { inflater, root -> ItemPhotoBinding.inflate(inflater, root, false) }
) {

  val radius = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_normal)

  itemView.setOnClickListener {
    clickListener(item)
  }

  bind {
    binding.moviePhoto.loadImage(item) {
      cornerRadius(radius)
      crop(CropOptions.CenterCrop)
    }
  }
}