# naverMAP

<br>

<br>

<div style="text-align : center">
    <img src="https://user-images.githubusercontent.com/63637706/106385909-e68ca580-6415-11eb-8085-75cd67d3e050.png" width="300" height="600">
    <img src="https://user-images.githubusercontent.com/63637706/106385910-e8566900-6415-11eb-9d42-5158b031fe38.png" width="300" height="600">
</div>
<br>

<br>


## 1. setMap

```kotlin
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
```

<br>

<br>

## 2. initMarker

```kotlin
private fun initMarker(): List<Marker> {
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
```

<br>

<br>

## 3. setLightness

```kotlin
private fun setLightness(markers: List<Marker>, p0: NaverMap) {
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
```

<br>

<br>

## 4. setUiSetting

```kotlin
private fun setUiSetting(p0: NaverMap) {
    val uiSettings = p0.uiSettings
    uiSettings.isZoomControlEnabled = true
    uiSettings.isScaleBarEnabled = true
    uiSettings.isCompassEnabled = true
    uiSettings.isLocationButtonEnabled = true
}
```

<br>

<br>

## 5. initCamera 

```kotlin
private fun initCamera(p0: NaverMap) {
    val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.47166814131256, 127.15149771207183))
        .animate(CameraAnimation.Fly)
    p0.defaultCameraAnimationDuration = 1000
    p0.moveCamera(cameraUpdate)
}
```
