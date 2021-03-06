package com.naver.map

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.naver.map.databinding.ActivityMainBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private val mainViewModel : MainViewModel by viewModels()
    private lateinit var locationSource: FusedLocationSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        setMap()
    }

    private fun setMap() {
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this@MainActivity)
    }

    override fun onMapReady(p0: NaverMap) {
        val markers = initMarker()

        setMarkers(markers, p0)
        setMarkerClick(markers)
        setLightness(markers, p0)
        setCameraListener(p0)
        setUiSetting(p0)
        setCurrentLocationIcon(p0)
        setMapClickListener(p0)
        initCamera(p0)

        p0.locationSource = locationSource
    }

    private fun initMarker() : List<Marker> {
        val markers = mutableListOf<Marker>()

        val marker = Marker()
        marker.position = LatLng(37.47166814131256, 127.15149771207183)

        val marker2 = Marker()
        marker2.position = LatLng(37.47148717476253, 127.15322826732239)

        val marker3 = Marker()
        marker3.position = LatLng(37.470618799063715, 127.15255865278681)

        markers.add(marker)
        markers.add(marker2)
        markers.add(marker3)

        return markers
    }

    private fun setLightness(markers : List<Marker>, p0: NaverMap) {
        mainViewModel.lightness.observe(this, Observer { lightness ->
            lightness?.let {
                for (i in markers.indices) {
                    if (lightness) {
                        markers[i].icon = MarkerIcons.BLACK
                        p0.lightness = 0f
                    } else {
                        markers[i].icon = MarkerIcons.YELLOW
                        p0.lightness = -0.8f
                    }
                }
            }
        })
    }

    private fun setMarkers(markers : List<Marker>, map : NaverMap) {
        for(element in markers) element.map = map
    }

    private fun setMarkerClick(markers : List<Marker>) {
        for (i in markers.indices) {
            markers[i].setOnClickListener {
                Toast.makeText(this, "${markers[i].position.latitude}, ${markers[i].position.longitude}", Toast.LENGTH_SHORT).show()
                true
            }
        }
    }

    private fun setCameraListener(p0 : NaverMap) {
        p0.addOnCameraIdleListener {
            Log.d("end", p0.cameraPosition.toString())
        }
    }

    private fun setUiSetting(p0 : NaverMap) {
        val uiSettings = p0.uiSettings
        uiSettings.isZoomControlEnabled = true // zoom
        uiSettings.isScaleBarEnabled = true // scale
        uiSettings.isCompassEnabled = true // compass
        uiSettings.isLocationButtonEnabled = true // nowLocation
    }

    private fun setCurrentLocationIcon(p0 : NaverMap) {
        val locationOverlay = p0.locationOverlay
        locationOverlay.isVisible = true
    }

    private fun initCamera(p0: NaverMap) {
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.47166814131256, 127.15149771207183))
            .animate(CameraAnimation.Fly)
        p0.defaultCameraAnimationDuration = 1000
        p0.moveCamera(cameraUpdate)
    }

    private fun setMapClickListener(p0: NaverMap) {
        p0.setOnMapClickListener { _, coord ->
            Toast.makeText(this, "${coord.latitude}, ${coord.longitude}", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}