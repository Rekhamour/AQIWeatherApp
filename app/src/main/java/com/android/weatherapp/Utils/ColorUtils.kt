package com.android.weatherapp.Utils

import android.content.Context
import com.android.weatherapp.R

class ColorUtils {
    companion object {

        public fun getColorCodeIndex(context: Context, aqi: Double): Int {
            if (aqi <= 50) {
                return context.resources.getColor(R.color.AQI_GOOD)
            } else if (aqi <= 100) {
                return context.resources.getColor(R.color.AQI_SATISFACTORY)
            } else if (aqi <= 200) {
                return context.resources.getColor(R.color.AQI_MODERATE)
            } else if (aqi <= 300) {
                return context.resources.getColor(R.color.AQI_POOR)
            } else if (aqi <= 400) {
                return context.resources.getColor(R.color.AQI_VERY_POOR)
            } else {
                return context.resources.getColor(R.color.AQI_SEVERE)
            }
        }


    }
}