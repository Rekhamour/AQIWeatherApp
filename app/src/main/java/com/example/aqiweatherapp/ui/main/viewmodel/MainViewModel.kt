package com.example.aqiweatherapp.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aqiweatherapp.Utils.LimitedSizeQueue
import com.example.aqiweatherapp.Utils.Resource
import com.example.aqiweatherapp.data.model.City
import com.example.aqiweatherapp.data.model.Stats

class MainViewModel() : ViewModel() {

    val cities = MutableLiveData<Resource<List<City>>>()
    val cityMap = MutableLiveData<HashMap<String,LimitedSizeQueue<Stats>>>()


    fun getCities(): LiveData<Resource<List<City>>> {
        return cities
    }

    fun getCityMap(): LiveData<HashMap<String,LimitedSizeQueue<Stats>>> {
        return cityMap
    }

}