package com.illiarb.tmdbclient.details.photos

import android.widget.ImageView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.core_ui_image.CropOptions
import com.illiarb.core_ui_image.RequestOptions.Companion.requestOptions
import com.illiarb.core_ui_image.loadImage
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener

/**
 * @author ilya-rb on 26.10.18.
 */
class PhotosAdapter(
    private val clickListener: OnClickListener = OnClickListener.DefaultOnClickListener()
) : ListDelegationAdapter<List<String>>(), OnClickListener by clickListener {

    init {
        delegatesManager.addDelegate(photoDelegate(clickListener))
    }

    private fun photoDelegate(clickListener: OnClickListener) =
        adapterDelegate<String, String>(R.layout.item_photo) {

            val radius = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_normal)
            val imageView = itemView.findViewById<ImageView>(R.id.moviePhoto)

            itemView.setOnClickListener {
                clickListener.onClick(item)
            }

            bind {
                imageView.loadImage(item, requestOptions {
                    cornerRadius(radius)
                    cropOptions(CropOptions.CENTER_CROP)
                })
            }
        }
}