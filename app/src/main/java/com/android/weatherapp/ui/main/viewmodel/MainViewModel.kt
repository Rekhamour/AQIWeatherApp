package com.android.weatherapp.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.weatherapp.Utils.FixedSizeList
import com.android.weatherapp.Utils.Resource
import com.android.weatherapp.data.model.City
import com.android.weatherapp.data.model.WeatherStats

// created by Rekha on 5/26/2021

class MainViewModel() : ViewModel() {

    val cities = MutableLiveData<Resource<List<City>>>()
    val cityMap = MutableLiveData<HashMap<String,FixedSizeList<WeatherStats>>>()


    fun getCities(): LiveData<Resource<List<City>>> {
        return cities
    }

    fun getCityMap(): LiveData<HashMap<String,FixedSizeList<WeatherStats>>> {
        return cityMap
    }

}