package com.illiarb.tmdbclient.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.core_ui_image.CropOptions
import com.illiarb.core_ui_image.ImageLoader
import com.illiarb.core_ui_image.RequestOptions
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.domain.NowPlayingSection
import kotlinx.android.synthetic.main.item_now_playing_section.view.*

fun nowPlayingDelegate(imageLoader: ImageLoader) =
    adapterDelegate<NowPlayingSection, MovieSection>(R.layout.item_now_playing_section) {

        itemView.nowPlayingPager.pageMargin =
            itemView.resources.getDimensionPixelSize(R.dimen.spacing_small)

        bind {
            itemView.nowPlayingPager.adapter = NowPlayingPagerAdapter(imageLoader, item.movies)
        }
    }

class NowPlayingPagerAdapter(
    private val imageLoader: ImageLoader,
    private val movies: List<Movie>
) : PagerAdapter() {

    override fun isViewFromObject(view: View, other: Any): Boolean = view == other

    override fun getCount(): Int = movies.size

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        val item = view as View
        container.removeView(item)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val spacing = container.resources.getDimensionPixelSize(R.dimen.corner_radius_normal)

        return LayoutInflater.from(container.context)
            .inflate(R.layout.item_now_playing_item, container, false)
            .apply {
                val title = findViewById<TextView>(R.id.itemNowPlayingTitle)
                val image = findViewById<ImageView>(R.id.itemNowPlayingImage)
                val movie = movies[position]

                movie.posterPath?.let {
                    imageLoader.fromUrl(it, image, RequestOptions.create {
                        cropOptions(CropOptions.CENTER_CROP)
                        cornerRadius(spacing)
                    })
                }

                title.text = movie.title
            }
            .also { container.addView(it) }
    }
}