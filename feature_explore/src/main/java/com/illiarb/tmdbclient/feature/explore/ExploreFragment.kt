package com.illiarb.tmdbclient.feature.explore

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.illiarb.tmdbclient.feature.explore.di.ExploreComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.state.UiState
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdblcient.core.entity.Location
import com.illiarb.tmdblcient.core.ext.addTo
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * @author ilya-rb on 31.10.18.
 */
class ExploreFragment : BaseFragment<ExploreViewModel>(), Injectable, OnMapReadyCallback, CoroutineScope {

    private var googleMap: GoogleMap? = null

    private val coroutinesJob = Job()

    override val coroutineContext: CoroutineContext
        get() = coroutinesJob + Dispatchers.Main

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

    override fun onDestroyView() {
        super.onDestroyView()
        coroutinesJob.cancel()
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

        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.google_maps_style))
        map.uiSettings.apply {
            isMyLocationButtonEnabled = true
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isRotateGesturesEnabled = true
        }

        // TODO Add Permissions Request
        // TODO Add Location settings check
        viewModel.fetchNearbyMovieTheaters()
    }

    private fun onTheatersStateChanged(state: UiState<List<Location>>) {
        if (state.isLoading()) {
            showProgressDialog()
        } else {
            hideProgressDialog()
        }

        if (state.hasData()) {
            showNearbyTheaters(state.requireData())
        }
    }

    private fun showNearbyTheaters(theaters: List<Location>) {
        googleMap?.let { map ->

            val currentLocation = LatLng(50.4390483, 30.4966947)

            val cameraUpdate =
                CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(currentLocation, 15f))

            map.animateCamera(cameraUpdate, object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    map.addMarker(
                        MarkerOptions()
                            .position(currentLocation)
                            .flat(true)
                            .icon(createMyLocationMarker())
                    )

                    placeMarkersOnMap(theaters, map)
                }

                override fun onCancel() {
                }
            })
        }
    }

    private fun placeMarkersOnMap(theaters: List<Location>, map: GoogleMap) {
        launch(context = coroutineContext) {
            val markerOptions = withContext(Dispatchers.Default) {
                theaters.map {
                    MarkerOptions()
                        .position(LatLng(it.lat, it.lon))
                        .flat(true)
                        .icon(createMapMarker(it.title))
                }
            }
            markerOptions.forEach { map.addMarker(it) }
        }
    }

    private fun createMyLocationMarker(): BitmapDescriptor {
        val icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_my_location)
            ?: return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)

        val bitmap = Bitmap.createBitmap(
            icon.intrinsicWidth,
            icon.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)

        icon.setBounds(0, 0, canvas.width, canvas.height)
        icon.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    // TODO Consider replacing implementation with view inflation
    // TODO And get bitmap from view
    private fun createMapMarker(title: String): BitmapDescriptor {
        val icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_nearby_theater)
            ?: return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            .apply {
                color = ContextCompat.getColor(requireContext(), R.color.colorOnSurface)
                textSize = resources.getDimensionPixelSize(R.dimen.text_subtitle_2).toFloat()
                textAlign = Paint.Align.LEFT
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }

        val baseline = -paint.ascent()
        val textWidth = paint.measureText(title)
        val textHeight = baseline + paint.descent()

        val bitmap = Bitmap.createBitmap(
            // Icon width + text width on the left
            icon.intrinsicWidth + textWidth.toInt(),
            Math.max(icon.intrinsicHeight, textHeight.toInt()),
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)

        icon.setBounds(0, 0, icon.intrinsicWidth, canvas.height)
        icon.draw(canvas)

        canvas.drawText(title, icon.intrinsicWidth.toFloat(), icon.intrinsicHeight / 2f, paint)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}