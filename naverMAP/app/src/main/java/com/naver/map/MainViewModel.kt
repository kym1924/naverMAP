package com.naver.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _lightness = MutableLiveData(true)
    val lightness : LiveData<Boolean> = _lightness

    fun setLightness() {
        _lightness.value = !_lightness.value!!
    }
}