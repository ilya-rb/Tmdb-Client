package com.illiarb.tmdbclient.feature.home.details.ui.photos

import android.view.ViewGroup
import com.illiarb.tmdbclient.feature.home.R
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter.BaseAdapter
import com.illiarb.tmdbexplorer.coreui.ext.inflate
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import javax.inject.Inject

/**
 * @author ilya-rb on 26.10.18.
 */
class PhotosAdapter @Inject constructor(private val imageLoader: ImageLoader) : BaseAdapter<String, PhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder =
        PhotoViewHolder(parent.inflate(R.layout.item_photo), imageLoader)
}