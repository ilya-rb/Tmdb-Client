package com.illiarb.tmdbclient.details.photos

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.core_ui_image.CropOptions
import com.illiarb.core_ui_image.ImageLoader
import com.illiarb.core_ui_image.RequestOptions
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import kotlinx.android.synthetic.main.item_photo.view.*
import javax.inject.Inject

/**
 * @author ilya-rb on 26.10.18.
 */
class PhotosAdapter @Inject constructor(
    imageLoader: ImageLoader,
    clickListener: OnClickListener
) : ListDelegationAdapter<List<String>>() {

    init {
        delegatesManager.addDelegate(photoDelegate(imageLoader, clickListener))
    }

    private fun photoDelegate(
        imageLoader: ImageLoader,
        clickListener: OnClickListener
    ) = adapterDelegate<String, String>(R.layout.item_photo) {

        val radius = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_normal)
        val imageView = itemView.moviePhoto

        itemView.setOnClickListener {
            clickListener.onClick(item)
        }

        bind {
            imageLoader.fromUrl(item, imageView, RequestOptions.create {
                cornerRadius(radius)
                cropOptions(CropOptions.CENTER_CROP)
            })
        }
    }
}