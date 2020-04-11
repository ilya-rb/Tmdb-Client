package com.illiarb.tmdbclient.ui.details.delegates

import android.widget.ImageView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.coreuiimage.CropOptions
import com.illiarb.coreuiimage.loadImage
import com.illiarb.tmdbclient.R
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.tmdbclient.servicetmdb.domain.Image

fun photoDelegate(clickListener: OnClickListener<Image>) = adapterDelegate<Image, Image>(R.layout.item_photo) {

  val radius = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_normal)
  val imageView = itemView.findViewById<ImageView>(R.id.moviePhoto)

  itemView.setOnClickListener {
    clickListener(item)
  }

  bind {
    imageView.loadImage(item) {
      cornerRadius(radius)
      crop(CropOptions.CenterCrop)
    }
  }
}