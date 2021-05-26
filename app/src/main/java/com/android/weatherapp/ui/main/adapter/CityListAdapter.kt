package com.android.weatherapp.ui.main.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.weatherapp.R
import com.android.weatherapp.Utils.ColorUtils
import com.android.weatherapp.Utils.TimeUtils
import com.android.weatherapp.data.model.City
import com.android.weatherapp.databinding.CityItemLayoutBinding
import com.android.weatherapp.ui.main.fragment.DetailFragment
import com.android.weatherapp.ui.main.view.MainActivity
import java.util.*

// created by Rekha on 5/26/2021

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
        DrawableCompat.setTint(
            wrappedDrawable,
            ColorUtils.getColorCodeIndex(context, list[position].aqi)
        )
        DrawableCompat.setTint(
            wrappedDrawable,
            ColorUtils.getColorCodeIndex(context, list[position].aqi)
        )
        holder.binding.cityUpdated.text = TimeUtils.getFormatedTime(list[position].lastUpdated)
        holder.itemView.setOnClickListener {
            val transaction = (context as MainActivity).supportFragmentManager.beginTransaction()
            val bundle = Bundle()
            bundle.putString("CITY", list[position].city)
            val fragment = DetailFragment();
            fragment.arguments = bundle
            transaction.setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            transaction.replace(R.id.frameLayout, fragment, "DetailFragment")
            transaction.addToBackStack("DetailFragment")
            transaction.commit()
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
}