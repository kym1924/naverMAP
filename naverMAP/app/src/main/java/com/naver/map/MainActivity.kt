package com.naver.map

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
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
import com.naver.maps.map.overlay.OverlayImage
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

        setCameraListener(p0)

        setUiSetting(p0)

        setCurrentLocationIcon(p0)

        p0.locationSource = locationSource

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.47166814131256, 127.15149771207183))
                .animate(CameraAnimation.Fly)
        p0.defaultCameraAnimationDuration = 1000
        p0.moveCamera(cameraUpdate)


        findViewById<Button>(R.id.btn_move).setOnClickListener{
            val setCameraMove = CameraUpdate.scrollTo(LatLng(37.556635, 126.908433))
                    .animate(CameraAnimation.Linear)
            p0.defaultCameraAnimationDuration = 1000
            p0.moveCamera(setCameraMove)
//            None: 애니메이션 없이 이동합니다. 기본값입니다.
//            Linear: 일정한 속도로 이동합니다.
//            Easing: 부드럽게 가감속하며 이동합니다. 가까운 거리를 이동할 때 적합합니다.
//            Fly: 부드럽게 축소됐다가 확대되며 이동합니다. 먼 거리를 이동할 때 적합합니다.
        }

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
        locationOverlay.icon = OverlayImage.fromResource(R.mipmap.ic_launcher)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}