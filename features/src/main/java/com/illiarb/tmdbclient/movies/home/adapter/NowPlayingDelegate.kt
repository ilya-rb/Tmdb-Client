package com.illiarb.tmdbclient.movies.home.adapter

import androidx.viewpager2.widget.ViewPager2
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.core_ui_image.CropOptions
import com.illiarb.core_ui_image.ImageLoader
import com.illiarb.core_ui_image.RequestOptions
import com.illiarb.core_ui_recycler_view.decoration.HorizontalDecoration
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdblcient.core.domain.entity.Movie
import com.illiarb.tmdblcient.core.domain.entity.MovieSection
import com.illiarb.tmdblcient.core.domain.entity.NowPlayingSection
import kotlinx.android.synthetic.main.item_now_playing_item.view.*
import kotlinx.android.synthetic.main.item_now_playing_section.view.*

fun nowPlayingDelegate(imageLoader: ImageLoader) =
    adapterDelegate<NowPlayingSection, MovieSection>(R.layout.item_now_playing_section) {

        val adapter = NowPlayingPagerAdapter(imageLoader)

        itemView.nowPlayingPager.adapter = adapter
        itemView.nowPlayingPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        bind {
            adapter.items = item.movies
            adapter.notifyDataSetChanged()
        }
    }

class NowPlayingPagerAdapter(imageLoader: ImageLoader) : ListDelegationAdapter<List<Movie>>() {

    init {
        delegatesManager.addDelegate(nowPlayingItemDelegate(imageLoader))
    }

    private fun nowPlayingItemDelegate(imageLoader: ImageLoader) =
        adapterDelegate<Movie, Movie>(R.layout.item_now_playing_item) {

            bind {
                itemView.itemNowPlayingTitle.text = item.title

                item.posterPath?.let {
                    imageLoader.fromUrl(it, itemView.itemNowPlayingImage, RequestOptions.create {
                        cropOptions(CropOptions.CENTER_CROP)
                    })
                }
            }
        }
}