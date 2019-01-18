package com.illiarb.tmdbclient.feature.home.photoview

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.illiarb.tmdbexplorer.coreui.ext.addToViewGroup
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import javax.inject.Inject

/**
 * @author ilya-rb on 18.01.19.
 */
class PhotoViewAdapter @Inject constructor(
    private val imageLoader: ImageLoader
) : PagerAdapter() {

    private val currentList = mutableListOf<String>()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(container.context)

        imageLoader.fromUrl(currentList[position], imageView)

        return imageView.also { it.addToViewGroup(container) }
    }

    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        container.removeView(item as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = currentList.size

    fun submitList(photos: Collection<String>) {
        currentList.addAll(photos)
        notifyDataSetChanged()
    }

    fun size(): Int = currentList.size

    fun indexOf(photo: String): Int {
        val index = currentList.indexOf(photo)
        return if (index == -1) 0 else index
    }
}