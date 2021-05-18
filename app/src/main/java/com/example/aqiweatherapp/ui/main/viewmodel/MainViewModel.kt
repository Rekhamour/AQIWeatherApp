package com.example.aqiweatherapp.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aqiweatherapp.Utils.Resource
import com.example.aqiweatherapp.data.model.City

class MainViewModel() : ViewModel() {

    val cities = MutableLiveData<Resource<List<City>>>()


    fun getCities(): LiveData<Resource<List<City>>> {
        return cities
    }

}