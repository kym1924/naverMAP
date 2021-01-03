package com.naver.map

import android.os.Bundle
import android.view.View
import android.widget.TextView
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
        setVisibility(binding)
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

        setMarkers(markers, p0)

        p0.locationSource = locationSource

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.47166814131256, 127.15149771207183))
                .animate(CameraAnimation.Linear)
        p0.defaultCameraAnimationDuration = 1000
        p0.moveCamera(cameraUpdate)

        val uiSettings = p0.uiSettings
        uiSettings.isZoomControlEnabled = false // zoom
        uiSettings.isScaleBarEnabled = false // scale
        uiSettings.isCompassEnabled = true // compass
        uiSettings.isLocationButtonEnabled = true // nowLocation

        mainViewModel.lightness.observe(this, Observer { lightness ->
            lightness?.let {
                if(lightness) {
                    setLightness(markers, lightness)
                    p0.lightness = 0f
                }
                else {
                    setLightness(markers, lightness)
                    p0.lightness = -0.8f
                }}
        })

        p0.setOnMapClickListener { point, coord ->
            Toast.makeText(this, "${coord.latitude}, ${coord.longitude}",
                    Toast.LENGTH_SHORT).show()
            mainViewModel.setMapClick()
        }

        marker.setOnClickListener {
            setMarkerClick(markers, 0)
            true
        }

        marker2.setOnClickListener {
            setMarkerClick(markers, 1)
            true
        }

        marker3.setOnClickListener {
            setMarkerClick(markers, 2)
            mainViewModel.setMarkerClick()
            true
        }
    }

    private fun setSize(markers : List<Marker>, index : Int) {
        for (i in markers.indices) {
            markers[i].height = Marker.SIZE_AUTO
            markers[i].width = Marker.SIZE_AUTO
        }
        markers[index].height = 150
        markers[index].width = 100
    }

    private fun setLightness(markers : List<Marker>, lightness : Boolean) {
        for (i in markers.indices) {
            if (lightness) {
                markers[i].icon = MarkerIcons.BLACK
            } else {
                markers[i].icon = MarkerIcons.YELLOW
            }
        }
    }

    private fun setMarkers(markers : List<Marker>, map : NaverMap) {
        for(element in markers) element.map = map
    }

    private fun setVisibility(binding : ActivityMainBinding) {
        mainViewModel.visibility.observe(this, Observer { visibility ->
            visibility?.let {
                if (visibility) {
                    binding.layoutMapClick.visibility = View.VISIBLE
                    binding.layoutMarkerClick.visibility = View.GONE
                } else {
                    binding.layoutMarkerClick.visibility = View.VISIBLE
                    binding.layoutMapClick.visibility = View.GONE
                }
            }
        })
    }

    private fun setMarkerClick(markers : List<Marker>, index : Int) {
        setSize(markers, index)
        mainViewModel.setMarkerClick()
        findViewById<TextView>(R.id.tv_latitude).text = markers[index].position.latitude.toString()
        findViewById<TextView>(R.id.tv_longitude).text = markers[index].position.longitude.toString()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}