package com.illiarb.tmdbclient.home.delegates

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.coreuiimage.CropOptions
import com.illiarb.coreuiimage.RequestOptions.Companion.requestOptions
import com.illiarb.coreuiimage.loadImage
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.domain.NowPlayingSection

fun nowPlayingDelegate(clickListener: OnClickListener) =
    adapterDelegate<NowPlayingSection, MovieSection>(R.layout.item_now_playing_section) {

        val nowPlayingPager = itemView.findViewById<RecyclerView>(R.id.nowPlayingPager)
        val nowPlayingTitle = itemView.findViewById<TextView>(R.id.nowPlayingTitle)
        val adapter = NowPlayingPagerAdapter(clickListener)
        val snapHelper = PagerSnapHelper()

        snapHelper.attachToRecyclerView(nowPlayingPager)

        nowPlayingPager.adapter = adapter
        nowPlayingPager.layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

        nowPlayingPager.addItemDecoration(
            SpaceDecoration.edgeInnerSpace(0, itemView.dimen(R.dimen.spacing_small))
        )

        bind {
            nowPlayingTitle.text = item.title
            adapter.items = item.movies
            adapter.notifyDataSetChanged()
        }
    }

private class NowPlayingPagerAdapter(private val clickListener: OnClickListener) :
    ListDelegationAdapter<List<Movie>>() {

    init {
        delegatesManager.addDelegate(nowPlayingDelegate())
    }

    private fun nowPlayingDelegate() =
        adapterDelegate<Movie, Movie>(R.layout.item_now_playing_item) {

            val title = itemView.findViewById<TextView>(R.id.itemNowPlayingTitle)
            val image = itemView.findViewById<ImageView>(R.id.itemNowPlayingImage)
            val spacing = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_normal)

            bind {
                title.text = item.title
                image.loadImage(item.backdropPath, requestOptions {
                    cropOptions(CropOptions.CENTER_CROP)
                    cornerRadius(spacing)
                })

                itemView.setOnClickListener {
                    clickListener.onClick(item)
                }
            }
        }
}