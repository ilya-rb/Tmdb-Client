package com.illiarb.tmdbclient.feature.movies.details.photos

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter.BaseAdapter
import com.illiarb.tmdbexplorer.coreui.ext.inflate
import javax.inject.Inject

/**
 * @author ilya-rb on 26.10.18.
 */
class PhotosAdapter @Inject constructor() : BaseAdapter<String, PhotoViewHolder>(diffCallback) {

    companion object {

        @JvmStatic
        private val diffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder =
        PhotoViewHolder(parent.inflate(R.layout.item_photo))
}