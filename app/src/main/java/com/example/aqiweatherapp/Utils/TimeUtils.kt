package com.example.aqiweatherapp.Utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TimeUtils {
    companion object{

        public fun getFormatedTimeHelper(milliSeconds: Long, dateFormat: String?): String {
            val formatter = SimpleDateFormat(dateFormat)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = milliSeconds
            return formatter.format(calendar.time)
        }

        public fun getFormatedTime(time: Long): String {
            try {
                val calendar: Calendar = Calendar.getInstance();
                calendar.timeInMillis = time
                val past: Date = calendar.time
                val now = Date()
                if (now.getTime() - past.getTime() < 60 * 1000) {
                    return "A few seconds ago"
                }
                else if(now.getTime() - past.getTime() < 60*60 * 1000){
                    return (TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime())
                        .toString() + " minutes ago")
                }
                else{
                    return getFormatedTimeHelper(time,"HH:mm a");
                }
            } catch (j: Exception) {
                j.printStackTrace()
            }
            return "N/A"
        }
    }
}