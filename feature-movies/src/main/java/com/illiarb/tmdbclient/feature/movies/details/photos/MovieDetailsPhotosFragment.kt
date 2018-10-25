package com.illiarb.tmdbclient.feature.movies.details.photos

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbclient.feature.movies.di.MoviesComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.decoration.GridItemDecoration
import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdblcient.core.entity.Backdrop
import kotlinx.android.synthetic.main.fragment_movie_details_photos.*
import javax.inject.Inject

class MovieDetailsPhotosFragment : BaseFragment<BaseViewModel>(), Injectable {

    companion object {

        const val SPAN_COUNT = 2
        const val ARG_PHOTOS = "photos"

        fun newInstance(backdrops: List<Backdrop>): MovieDetailsPhotosFragment =
            MovieDetailsPhotosFragment().apply {
                Bundle()
                    .apply {
                        backdrops
                            .map { it.filePath }
                            .toTypedArray()
                            .also { putStringArray(ARG_PHOTOS, it) }
                    }
                    .also { arguments = it }
            }
    }

    @Inject
    lateinit var photosAdapter: PhotosAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieDetailsPhotos.let {
            it.adapter = photosAdapter
            it.layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)

            val space = resources.getDimensionPixelSize(R.dimen.movie_details_horizontal_small_margin)
            it.addItemDecoration(GridItemDecoration(space, SPAN_COUNT))
        }

        arguments?.let { args ->
            args.getStringArray(ARG_PHOTOS)?.let { photos ->
                photosAdapter.submitList(photos.toList())
            }
        }
    }

    override fun inject(appProvider: AppProvider) = MoviesComponent.get(appProvider, requireActivity()).inject(this)

    override fun getContentView(): Int = R.layout.fragment_movie_details_photos

    override fun getViewModelClass(): Class<BaseViewModel> = BaseViewModel::class.java
}