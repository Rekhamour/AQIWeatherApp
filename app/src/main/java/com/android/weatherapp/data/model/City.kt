package com.android.weatherapp.data.model

// created by Rekha on 5/26/2021

class City(val city: String,val aqi: Double,val lastUpdated:Long):Comparable<City> {
    override fun compareTo(other: City): Int {
        return (this.lastUpdated-other.lastUpdated)as Int
    }

    override fun toString(): String {
        return this.city+"         -         "+this.aqi
    }
}

