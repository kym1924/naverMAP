package com.naver.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _lightness = MutableLiveData(true)
    val lightness : LiveData<Boolean> = _lightness

    private val _visibility = MutableLiveData(true)
    val visibility : LiveData<Boolean> = _visibility

    fun setLightness() {
        _lightness.value = !_lightness.value!!
    }

    fun setMapClick() {
        _visibility.value = true
    }

    fun setMarkerClick() {
        _visibility.value = false
    }
}