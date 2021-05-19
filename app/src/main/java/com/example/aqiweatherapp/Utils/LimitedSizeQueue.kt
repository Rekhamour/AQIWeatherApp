package com.example.aqiweatherapp.Utils

class LimitedSizeQueue<K>(private val maxSize: Int) : ArrayList<K>() {
    override fun add(k: K): Boolean {
        val r = super.add(k)
        if (size > maxSize) {
            removeRange(0, size - maxSize)
        }
        return r
    }

    val youngest: K?
        get() = get(size - 1)
    val oldest: K?
        get() = get(0)

}