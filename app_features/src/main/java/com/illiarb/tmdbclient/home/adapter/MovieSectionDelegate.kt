package com.illiarb.tmdbclient.home.adapter

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.core_ui_image.CropOptions
import com.illiarb.core_ui_image.ImageLoader
import com.illiarb.core_ui_image.RequestOptions
import com.illiarb.core_ui_recycler_view.LayoutOrientation
import com.illiarb.core_ui_recycler_view.LayoutType
import com.illiarb.core_ui_recycler_view.RecyclerViewBuilder
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdblcient.core.domain.ListSection
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieSection
import kotlinx.android.synthetic.main.item_movie.view.*
import kotlinx.android.synthetic.main.item_movie_section.view.*

fun movieSectionDelegate(
    imageLoader: ImageLoader,
    clickListener: OnClickListener
) = adapterDelegate<ListSection, MovieSection>(R.layout.item_movie_section) {

    val adapter = MovieSectionAdapter(imageLoader, clickListener)

    val sectionTitle = itemView.itemSectionTitle
    val sectionList = itemView.itemMovieSectionList

    RecyclerViewBuilder
        .create {
            adapter(adapter)
            type(LayoutType.Linear())
            orientation(LayoutOrientation.HORIZONTAL)
            hasFixedSize(true)
            spaceBetween {
                spacing = itemView.resources.getDimensionPixelSize(R.dimen.spacing_normal)
            }
        }
        .setupWith(sectionList)

    bind {
        sectionTitle.text = item.title
        adapter.items = item.movies
        adapter.notifyDataSetChanged()
    }
}

class MovieSectionAdapter(
    imageLoader: ImageLoader,
    clickListener: OnClickListener
) : ListDelegationAdapter<List<Movie>>() {

    init {
        delegatesManager.addDelegate(movieDelegate(imageLoader, clickListener))
    }

    private fun movieDelegate(
        imageLoader: ImageLoader,
        clickListener: OnClickListener
    ) = adapterDelegate<Movie, Movie>(R.layout.item_movie) {

        val image = itemView.itemMoviePoster
        val imageCornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.spacing_small)

        bind {
            imageLoader.fromUrl(item.posterPath, image, RequestOptions.create {
                cornerRadius(imageCornerRadius)
                cropOptions(CropOptions.CENTER_CROP)
                thumbnail(0.2f)
            })

            itemView.setOnClickListener {
                clickListener.onClick(item)
            }
        }
    }
}