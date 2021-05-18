package com.example.aqiweatherapp.ui.main.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.aqiweatherapp.R
import com.example.aqiweatherapp.Utils.TimeUtils
import com.example.aqiweatherapp.data.model.City
import com.example.aqiweatherapp.databinding.CityItemLayoutBinding
import com.example.aqiweatherapp.ui.main.fragment.GraphFragment
import com.example.aqiweatherapp.ui.main.view.MainActivity
import java.util.*


class CityListAdapter(val context: Context, val list: ArrayList<City>) :
    RecyclerView.Adapter<CityListAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: CityItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CityItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.cityName.text = list[position].city
        holder.binding.cityAqi.text = String.format("%.2f", list[position].aqi)
        val unwrappedDrawable = holder.binding.cityAqi.background
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(wrappedDrawable, getColorCodeIndex(list[position].aqi))
        DrawableCompat.setTint(wrappedDrawable,getColorCodeIndex(list[position].aqi))
        holder.binding.cityUpdated.text = TimeUtils.getFormatedTime(list[position].lastUpdated)
        holder.itemView.setOnClickListener {
            val transaction = (context as MainActivity).supportFragmentManager.beginTransaction()
            val bundle = Bundle()
            bundle.putString("CITY", list[position].city)
            val fragment =  GraphFragment();
            fragment.arguments = bundle
            transaction.replace(R.id.frameLayout, fragment,"GraphFragment")
            transaction.addToBackStack("GraphFragment")
            transaction.commit()
        }
    }

    private fun getColorCodeIndex(aqi:Double):Int{
        if(aqi<=50){
            return context.resources.getColor(R.color.AQI_GOOD)
        }
        else if(aqi<=100){
            return context.resources.getColor(R.color.AQI_SATISFACTORY)
        }else if(aqi<=200){
            return context.resources.getColor(R.color.AQI_MODERATE)
        }else if(aqi<=300){
            return context.resources.getColor(R.color.AQI_POOR)
        }else if(aqi<=400){
            return context.resources.getColor(R.color.AQI_VERY_POOR)
        }else{
            return context.resources.getColor(R.color.AQI_SEVERE)
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }
}