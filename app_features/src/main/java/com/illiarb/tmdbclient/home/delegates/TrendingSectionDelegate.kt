package com.illiarb.tmdbclient.home.delegates

import android.os.Parcelable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.coreuiimage.CropOptions
import com.illiarb.coreuiimage.loadImage
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.RecyclerViewStateSaver
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.StateSaver
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.domain.TrendingSection
import com.illiarb.tmdblcient.core.domain.TrendingSection.TrendingItem

private const val KEY_TRENDING_STATE = "trending_state"

@Suppress("LongMethod")
fun trendingSectionDelegate(
    stateSaver: RecyclerViewStateSaver,
    clickListener: OnClickListener
) = adapterDelegate<TrendingSection, MovieSection>(R.layout.item_trending_section) {

    val adapter = TrendingSectionAdapter(clickListener)
    val trendingList = itemView.findViewById<RecyclerView>(R.id.itemTrendingSectionList)
    val stateCallback: StateSaver = {
        putParcelable(KEY_TRENDING_STATE, trendingList.layoutManager?.onSaveInstanceState())
    }

    trendingList.let {
        it.adapter = adapter
        it.layoutManager = LinearLayoutManager(
            itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        it.addItemDecoration(
            SpaceDecoration.edgeInnerSpace(
                it.dimen(R.dimen.spacing_normal),
                it.dimen(R.dimen.spacing_small)
            )
        )
    }

    bind {
        adapter.items = item.items
        adapter.notifyDataSetChanged()

        val state = stateSaver.state(KEY_TRENDING_STATE) as? Parcelable?
        trendingList.layoutManager?.onRestoreInstanceState(state)
    }

    onViewAttachedToWindow {
        stateSaver.registerStateSaver(KEY_TRENDING_STATE, stateCallback)
    }

    onViewDetachedFromWindow {
        stateSaver.unregisterStateSaver(KEY_TRENDING_STATE)
    }
}

private class TrendingSectionAdapter(clickListener: OnClickListener) :
    ListDelegationAdapter<List<TrendingItem>>() {

    init {
        delegatesManager.addDelegate(trendingDelegate(clickListener))
    }

    private fun trendingDelegate(clickListener: OnClickListener) =
        adapterDelegate<TrendingItem, TrendingItem>(R.layout.item_trending) {

            val image = itemView.findViewById<ImageView>(R.id.itemTrendingImage)
            val name = itemView.findViewById<TextView>(R.id.itemTrendingName)

            bind {
                name.text = item.name
                image.loadImage(item.image) {
                    crop(CropOptions.Circle)
                }

                itemView.setOnClickListener {
                    clickListener(item)
                }
            }
        }
}