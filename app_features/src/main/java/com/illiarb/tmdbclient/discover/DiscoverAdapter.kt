package com.illiarb.tmdbclient.discover

import androidx.lifecycle.Observer
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.illiarb.tmdbclient.common.delegates.movieDelegate
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdbexplorer.coreui.common.SizeSpec
import com.illiarb.tmdblcient.core.domain.Movie

class DiscoverAdapter(
    private val clickListener: OnClickListener = OnClickListener.DefaultOnClickListener()
) : ListDelegationAdapter<List<Movie>>(), Observer<List<Movie>>, OnClickListener by clickListener {

    init {
        delegatesManager.addDelegate(
            movieDelegate(
                this,
                SizeSpec.MatchParent,
                SizeSpec.Fixed(R.dimen.discover_item_movie_height)
            )
        )
    }

    override fun onChanged(movies: List<Movie>) {
        items = movies
        notifyDataSetChanged()
    }
}