package com.example.aqiweatherapp.data.model

class Stats(val aqi: Double,val lastUpdated:Long) {
    override fun toString(): String {
        return "$aqi || $lastUpdated"
    }
}