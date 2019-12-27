package com.illiarb.tmdbclient.discover

import androidx.lifecycle.Observer
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.illiarb.tmdbclient.common.delegates.movieDelegate
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdblcient.core.domain.Movie

class DiscoverAdapter(
    private val clickListener: OnClickListener = OnClickListener.DefaultOnClickListener()
) : ListDelegationAdapter<List<Movie>>(), Observer<List<Movie>>, OnClickListener by clickListener {

    init {
        delegatesManager.addDelegate(movieDelegate(this))
    }

    override fun onChanged(movies: List<Movie>) {
        items = movies
        notifyDataSetChanged()
    }
}