package com.android.weatherapp.data.model

// created by Rekha on 5/26/2021

class WeatherStats(val aqi: Double, val lastUpdated:Long) {
    override fun toString(): String {
        return "$aqi || $lastUpdated"
    }
}