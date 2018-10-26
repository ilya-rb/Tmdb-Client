package com.illiarb.tmdbclient.feature.movies.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.illiarb.tmdbclient.feature.movies.R
import com.illiarb.tmdbclient.feature.movies.di.MoviesComponent
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider

/**
 * @author ilya-rb on 26.10.18.
 */
class MovieFiltersFragment : BottomSheetDialogFragment(), Injectable {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_movie_filters, container, false)

    override fun inject(appProvider: AppProvider) = MoviesComponent.get(appProvider, requireActivity()).inject(this)
}