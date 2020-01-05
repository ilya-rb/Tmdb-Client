package com.illiarb.tmdbclient.home.delegates

import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.core_ui_image.CropOptions
import com.illiarb.core_ui_image.RequestOptions.Companion.requestOptions
import com.illiarb.core_ui_image.loadImage
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.ext.updatePadding
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.domain.NowPlayingSection
import java.util.Timer
import java.util.concurrent.TimeUnit

fun nowPlayingDelegate(clickListener: OnClickListener) =
    adapterDelegate<NowPlayingSection, MovieSection>(R.layout.item_now_playing_section) {

        val nowPlayingPager = itemView.findViewById<ViewPager2>(R.id.nowPlayingPager)
        val nowPlayingTitle = itemView.findViewById<TextView>(R.id.nowPlayingTitle)
        val adapter = NowPlayingPagerAdapter(clickListener)
        val spacing = itemView.dimen(R.dimen.spacing_normal)

        val timerHandler = Handler(Looper.getMainLooper())
        val updateInterval = TimeUnit.SECONDS.toMillis(5L)
        var bannerTimer: Timer? = null

        nowPlayingPager.adapter = adapter

        // Set offscreen page limit to at least 1
        // so adjacent pages are always laid out
        nowPlayingPager.offscreenPageLimit = 1
        nowPlayingPager.addItemDecoration(SpaceDecoration(spacingRight = spacing))

        // TODO: This will probable will be exposed in later versions
        //  not to rely on getChildAt(0) which might break
        //  setting padding on inner RecyclerView puts over scroll effect in the right place
        val recyclerView = nowPlayingPager.getChildAt(0) as RecyclerView
        recyclerView.clipToPadding = false
        recyclerView.updatePadding(left = spacing, right = spacing)

        bind {
            nowPlayingTitle.text = item.title
            adapter.items = item.movies
            adapter.notifyDataSetChanged()

            // TODO: Fix timer
//            bannerTimer = Timer()
//            bannerTimer?.scheduleAtFixedRate(object : TimerTask() {
//                override fun run() {
//                    // Post to main thread as timer operates in a different one
//                    timerHandler.post {
//                        nowPlayingPager.switchToNextPosition()
//                    }
//                }
//            }, updateInterval, updateInterval)
        }

        onViewDetachedFromWindow {
            timerHandler.removeCallbacksAndMessages(null)
            bannerTimer?.cancel()
        }
    }

class NowPlayingPagerAdapter(private val clickListener: OnClickListener) :
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