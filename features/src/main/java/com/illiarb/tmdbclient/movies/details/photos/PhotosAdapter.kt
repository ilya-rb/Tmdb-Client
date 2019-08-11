package com.illiarb.tmdbclient.movies.details.photos

import android.view.ViewGroup
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.ext.inflate
import com.illiarb.core_ui_image.ImageLoader
import com.illiarb.core_ui_recycler_view.SimpleAdapter
import javax.inject.Inject

/**
 * @author ilya-rb on 26.10.18.
 */
class PhotosAdapter @Inject constructor(private val imageLoader: ImageLoader) :
    SimpleAdapter<String, PhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder =
        PhotoViewHolder(parent.inflate(R.layout.item_photo), imageLoader)

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(items[position])
    }
}