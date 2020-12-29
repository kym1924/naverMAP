package com.naver.map

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.naver.map.databinding.ActivityMainBinding
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback

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
        mainViewModel.lightness.observe(this, Observer { lightness ->
            lightness?.let { if(lightness) p0.lightness = 0f else p0.lightness = -0.8f }
        })
    }

}