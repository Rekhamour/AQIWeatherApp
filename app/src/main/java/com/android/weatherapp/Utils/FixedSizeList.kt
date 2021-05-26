package com.android.weatherapp.Utils

// created by Rekha on 5/26/2021
class FixedSizeList<K>(private val maxSize: Int) : ArrayList<K>() {
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