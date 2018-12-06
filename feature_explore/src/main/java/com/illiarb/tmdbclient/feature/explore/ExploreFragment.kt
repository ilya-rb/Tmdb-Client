package com.illiarb.tmdbclient.feature.explore

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.illiarb.tmdbclient.feature.explore.adapter.TheatersAdapter
import com.illiarb.tmdbclient.feature.explore.di.ExploreComponent
import com.illiarb.tmdbexplorer.coreui.base.BaseFragment
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.decoration.SpaceItemDecoration
import com.illiarb.tmdbexplorer.coreui.state.UiState
import com.illiarb.tmdbexplorerdi.Injectable
import com.illiarb.tmdbexplorerdi.providers.AppProvider
import com.illiarb.tmdblcient.core.entity.Location
import com.illiarb.tmdblcient.core.ext.addTo
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author ilya-rb on 31.10.18.
 */
class ExploreFragment : BaseFragment<ExploreViewModel>(), Injectable, OnMapReadyCallback, CoroutineScope {

    @Inject
    lateinit var adapter: TheatersAdapter

    private var googleMap: GoogleMap? = null

    private val snapHelper = PagerSnapHelper()

    private val snapScrollListener = createSnapScrollListener()

    private val coroutinesJob = Job()

    override val coroutineContext: CoroutineContext
        get() = coroutinesJob + Dispatchers.Main

    override fun getContentView(): Int = R.layout.fragment_explore

    override fun getViewModelClass(): Class<ExploreViewModel> = ExploreViewModel::class.java

    override fun inject(appProvider: AppProvider) = ExploreComponent.get(appProvider, requireActivity()).inject(this)

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val map = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        map.getMapAsync(this)

        theatersList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = this@ExploreFragment.adapter
            snapHelper.attachToRecyclerView(this)
            addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.margin_small), 0))

            val displayWidth = getDisplayWidth()
            setPadding(displayWidth / 2, 0, 0, 0)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.observeNearbyTheaters()
            .subscribe(::onTheatersStateChanged, Throwable::printStackTrace)
            .addTo(destroyViewDisposable)
    }

    override fun onResume() {
        super.onResume()
        theatersList.addOnScrollListener(snapScrollListener)
    }

    override fun onPause() {
        super.onPause()
        theatersList.removeOnScrollListener(snapScrollListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutinesJob.cancel()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        launch(context = coroutineContext) {
            val styleOptions = withContext(Dispatchers.Default) {
                MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.google_maps_style)
            }
            map.setMapStyle(styleOptions)
            map.uiSettings.isRotateGesturesEnabled = true

            // TODO Add Permissions Request
            // TODO Add Location settings check
            viewModel.fetchNearbyMovieTheaters()
        }
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

    private fun moveCameraToLocation(location: Location) {
        googleMap?.let { map ->
            val position = LatLng(location.lat, location.lon)
            val cameraUpdate = CameraUpdateFactory.newLatLng(position)
            map.animateCamera(cameraUpdate)
        }
    }

    private fun showNearbyTheaters(theaters: List<Location>) {
        theatersCount.text = getString(R.string.theaters_count_text, theaters.size)

        googleMap?.let { map ->
            if (theaters.isEmpty()) {
                return@let
            }

            val firstPlace = theaters.first()
            val currentLocation = LatLng(firstPlace.lat, firstPlace.lon)

            val cameraUpdate =
                CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(currentLocation, 15f))

            map.animateCamera(cameraUpdate, object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    launch(context = coroutineContext) {
                        val myLocationMarker = withContext(Dispatchers.Default) { createMyLocationMarker() }

                        map.addMarker(
                            MarkerOptions()
                                .position(LatLng(50.4390483, 30.4966947))
                                .flat(true)
                                .icon(myLocationMarker)
                        )
                    }

                    placeMarkersOnMap(theaters, map)

                    adapter.submitList(theaters)

                    theatersList.post {
                        theatersList.smoothScrollToPosition(0)
                    }
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
                        .icon(createMarkerFromView(it.title))
                }
            }

            markerOptions.forEach {
                map.addMarker(it)
            }
        }
    }

    private fun createSnapScrollListener(): RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // When recycler scroll is stops
                // Start changing position
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    theatersList.layoutManager?.let { manager ->
                        val snapView = snapHelper.findSnapView(manager)
                        snapView?.let {
                            val position = manager.getPosition(it)
                            moveCameraToLocation(this@ExploreFragment.adapter.getItemAt(position))
                        }
                    }
                }
            }
        }

    private fun getDisplayWidth(): Int {
        val display = requireActivity().windowManager.defaultDisplay
        val outSize = Point().also { display.getSize(it) }
        return outSize.x
    }

    private suspend fun createMyLocationMarker(): BitmapDescriptor = coroutineScope {
        val icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_my_location)
            ?: return@coroutineScope BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)

        val bitmap = Bitmap.createBitmap(
            icon.intrinsicWidth,
            icon.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)

        icon.setBounds(0, 0, canvas.width, canvas.height)
        icon.draw(canvas)

        BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private suspend fun createMarkerFromView(title: String): BitmapDescriptor = coroutineScope {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.item_nearby_theater_marker, null, false)
            .apply {
                findViewById<TextView>(R.id.itemNearbyTitle).text = title
            }

        val specWidth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(specWidth, specWidth)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        view.draw(canvas)

        BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}