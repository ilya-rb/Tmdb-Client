package com.illiarb.tmdbclient.feature.explore

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.illiarb.tmdbclient.feature.explore.di.ExploreComponent
import com.illiarb.tmdbclient.feature.explore.exception.PermissionDeniedException
import com.illiarb.tmdbclient.feature.explore.settings.SettingsChecker
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.permission.PermissionResolver
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdblcient.core.entity.Location
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

    override fun getContentView(): Int = R.layout.fragment_explore

    override fun getViewModelClass(): Class<ExploreViewModel> = ExploreViewModel::class.java

    override fun inject(appProvider: AppProvider) = ExploreComponent.get(appProvider, requireActivity()).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.observeNearbyTheaters()
            .subscribe(::onTheatersStateChanged, Throwable::printStackTrace)
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

        permissionResolver.requestPermissions(
            PermissionResolver.PERMISSION_COARSE_LOCATION,
            PermissionResolver.PERMISSION_FINE_LOCATION
        )
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

    private fun onTheatersStateChanged(data: List<Location>) {
        showNearbyTheaters(data)
    }

    private fun showNearbyTheaters(theaters: List<Location>) {
        googleMap?.let { map ->

            map.uiSettings.apply {
                isMyLocationButtonEnabled = true
                isZoomControlsEnabled = true
            }

            val currentLocation = LatLng(50.4390483, 30.4966947)
            map.addMarker(
                MarkerOptions()
                    .position(currentLocation)
                    .flat(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            )

            val cameraUpdate =
                CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(currentLocation, 15f))

            map.animateCamera(cameraUpdate, object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    theaters.forEach { (lat, lon) ->
                        MarkerOptions()
                            .position(LatLng(lat, lon))
                            .flat(true)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            .also { map.addMarker(it) }
                    }

                    map.addCircle(
                        CircleOptions()
                            .center(currentLocation)
                            .fillColor(Color.TRANSPARENT)
                            .strokeColor(Color.BLUE)
                            .strokeWidth(1.5f)
                            .radius(1000.0)
                    )
                }

                override fun onCancel() {
                }
            })
        }
    }

    private fun showPermissionDeniedError() {
    }
}