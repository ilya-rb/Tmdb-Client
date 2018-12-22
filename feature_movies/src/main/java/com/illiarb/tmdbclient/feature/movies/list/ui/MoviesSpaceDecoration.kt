package com.illiarb.tmdbclient.feature.movies.list.ui

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbclient.feature.movies.list.ui.delegates.nowplaying.NowPlayingSectionDelegate

// TODO: Simplify
class MoviesSpaceDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val spacing = context.resources.getDimensionPixelSize(R.dimen.item_movie_spacing)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val viewHolder = parent.getChildViewHolder(view)

        if (viewHolder is NowPlayingSectionDelegate.ViewHolder) {
            outRect.left = spacing / 4
            outRect.right = spacing / 4
        } else {
            outRect.left = spacing / 2
            outRect.right = spacing / 2

            if (parent.getChildAdapterPosition(view) == parent.childCount - 1) {
                outRect.bottom = spacing / 2
            }
        }
    }
}