package com.naver.map

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.naver.map.databinding.ActivityMainBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private val mainViewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this

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
        val marker = Marker()
        marker.position = LatLng(37.47166814131256, 127.15149771207183)

        val marker2 = Marker()
        marker2.position = LatLng(37.47148717476253, 127.15322826732239)

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
                    marker.icon = MarkerIcons.BLACK
                    marker2.icon = MarkerIcons.BLACK
                    p0.lightness = 0f
                }
                else {
                    marker.icon = MarkerIcons.YELLOW
                    marker2.icon = MarkerIcons.YELLOW
                    p0.lightness = -0.8f
                }}
            marker.map = p0
            marker2.map = p0
        })

        p0.setOnMapClickListener { point, coord ->
            Toast.makeText(this, "${coord.latitude}, ${coord.longitude}",
                    Toast.LENGTH_SHORT).show()
        }

        marker.setOnClickListener {
            if(marker.height == 120) marker.height= Marker.SIZE_AUTO else marker.height=120
            if(marker.width == 80) marker.width= Marker.SIZE_AUTO else marker.width=80
            true
        }

        marker2.setOnClickListener {
            if(marker2.height == 120) marker2.height= Marker.SIZE_AUTO else marker2.height=120
            if(marker2.width == 80) marker2.width= Marker.SIZE_AUTO else marker2.width=80
            true
        }
    }
}