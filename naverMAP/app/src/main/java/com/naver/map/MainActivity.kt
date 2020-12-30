package com.naver.map

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.naver.map.databinding.ActivityMainBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
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
        marker.position = LatLng(37.5670135, 126.9783740)

        val marker2 = Marker()
        marker2.position = LatLng(37.5651279, 126.9767904)

        val marker3 = Marker()
        marker3.position = LatLng(37.5625365, 126.9832241)

        mainViewModel.lightness.observe(this, Observer { lightness ->
            lightness?.let {
                if(lightness) {
                    marker.icon = MarkerIcons.BLACK
                    marker2.icon = MarkerIcons.BLACK
                    marker3.icon = MarkerIcons.BLACK
                    marker.map = p0
                    marker2.map = p0
                    marker3.map = p0
                    p0.lightness = 0f
                }
                else {
                    marker.icon = MarkerIcons.YELLOW
                    marker2.icon = MarkerIcons.YELLOW
                    marker3.icon = MarkerIcons.YELLOW
                    marker3.map = null
                    p0.lightness = -0.8f
                }}
        })
    }

}