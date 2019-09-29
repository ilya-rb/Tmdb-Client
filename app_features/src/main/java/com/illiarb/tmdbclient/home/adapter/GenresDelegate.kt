package com.illiarb.tmdbclient.home.adapter

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.core_ui_recycler_view.LayoutOrientation
import com.illiarb.core_ui_recycler_view.LayoutType
import com.illiarb.core_ui_recycler_view.RecyclerViewBuilder
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.GenresSection
import com.illiarb.tmdblcient.core.domain.MovieSection
import kotlinx.android.synthetic.main.item_genre.view.*
import kotlinx.android.synthetic.main.item_genres_section.view.*

fun genresSectionDelegate() =
    adapterDelegate<GenresSection, MovieSection>(R.layout.item_genres_section) {

        val adapter = GenreAdapter()

        RecyclerViewBuilder
            .create {
                adapter(adapter)
                type(LayoutType.Linear())
                orientation(LayoutOrientation.HORIZONTAL)
                spaceBetween {
                    spacing = itemView.resources.getDimensionPixelSize(R.dimen.spacing_normal)
                }
            }
            .setupWith(itemView.itemGenresSectionList)

        bind {
            adapter.items = item.genres
            adapter.notifyDataSetChanged()
        }
    }

class GenreAdapter : ListDelegationAdapter<List<Genre>>() {

    init {
        delegatesManager.addDelegate(genreDelegate())
    }

    private fun genreDelegate() = adapterDelegate<Genre, Genre>(R.layout.item_genre) {
        bind {
            itemView.itemGenre.text = item.name
        }
    }
}