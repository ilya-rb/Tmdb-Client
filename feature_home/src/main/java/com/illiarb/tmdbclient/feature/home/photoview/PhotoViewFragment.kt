package com.illiarb.tmdbclient.feature.home.photoview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.illiarb.tmdbclient.feature.home.R
import com.illiarb.tmdbclient.feature.home.di.MoviesComponent
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.navigation.NavigationKeys
import kotlinx.android.synthetic.main.fragment_photo_view.*
import javax.inject.Inject

/**
 * @author ilya-rb on 17.01.19.
 */
class PhotoViewFragment : Fragment(), Injectable {

    @Inject
    lateinit var photosAdapter: PhotoViewAdapter

    override fun inject(appProvider: AppProvider) = MoviesComponent.get(appProvider).inject(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photo_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val photos = it.getStringArrayList(NavigationKeys.EXTRA_PHOTOS)
            if (photos != null) {
                photosAdapter.submitList(photos)
            }

            photosPager.adapter = photosAdapter

            val selected = it.getString(NavigationKeys.EXTRA_SELECTED_PHOTO)
            if (selected != null) {
                photosPager.currentItem = photosAdapter.indexOf(selected)
            }
        }
    }
}