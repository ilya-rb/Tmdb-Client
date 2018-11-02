package com.illiarb.tmdbclient.feature.explore

import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.illiarb.tmdbclient.feature.explore.di.ExploreComponent
import com.illiarb.tmdbclient.feature.explore.exception.PermissionDeniedException
import com.illiarb.tmdbclient.feature.explore.settings.SettingsChecker
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.permission.PermissionResolver
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdblcient.core.ext.addTo
import io.reactivex.Completable
import kotlinx.android.synthetic.main.fragment_explore.*
import javax.inject.Inject

/**
 * @author ilya-rb on 31.10.18.
 */
class ExploreFragment : BaseFragment<ExploreViewModel>(), Injectable, OnMapReadyCallback {

    @Inject
    lateinit var permissionResolver: PermissionResolver

    @Inject
    lateinit var settingsChecker: SettingsChecker

    private var googleMap: GoogleMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        permissionResolver.requestPermissions(PermissionResolver.PERMISSION_LOCATION)
            .flatMapCompletable { permissions ->
                if (permissions.all { it.isGranted }) {
                    settingsChecker.checkLocationSettings()
                } else {
                    Completable.error(PermissionDeniedException())
                }
            }
            .subscribe(
                { viewModel.fetchNearbyMovieTheaters() },
                {
                    if (it is PermissionDeniedException) {
                        showPermissionDeniedError()
                    }
                }
            )
            .addTo(destroyViewDisposable)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
    }

    override fun getContentView(): Int = R.layout.fragment_explore

    override fun getViewModelClass(): Class<ExploreViewModel> = ExploreViewModel::class.java

    override fun inject(appProvider: AppProvider) = ExploreComponent.get(appProvider, requireActivity()).inject(this)

    private fun showPermissionDeniedError() {
    }
}