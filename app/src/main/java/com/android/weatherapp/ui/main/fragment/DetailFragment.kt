package com.android.weatherapp.ui.main.fragment

import android.graphics.DashPathEffect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.android.weatherapp.R
import com.android.weatherapp.Utils.ColorUtils
import com.android.weatherapp.Utils.FixedSizeList
import com.android.weatherapp.Utils.TimeUtils
import com.android.weatherapp.data.model.WeatherStats
import com.android.weatherapp.databinding.FragmentGraphBinding
import com.android.weatherapp.ui.main.viewmodel.MainViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter

// created by Rekha on 5/26/2021

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentGraphBinding;
    private lateinit var mainViewModel: MainViewModel;
    private lateinit var values: ArrayList<BarEntry>;
    private lateinit var labels: Array<String>
    private lateinit var colors: ArrayList<Int>
    private val TAG = "DetailFragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGraphBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        initChart()
        setupViewModel()
    }

    fun setUpUI() {
        binding.backBtn.setOnClickListener {
            fragmentManager!!.popBackStackImmediate()
        }
        binding.cityName.text = this.arguments!!.getString("CITY").toString()
    }

    fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(
            activity!!
        ).get(MainViewModel::class.java)

        mainViewModel.cityMap.observe(this, {
            if (it.containsKey(binding.cityName.text)) {
                updateChart(it.get(binding.cityName.text)!!);
            }
        })
    }

    private fun initChart(){
        binding.chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.chart.xAxis.setGranularity(1f);
        binding.chart.xAxis.setGranularityEnabled(true);
        binding.chart.legend.isEnabled = false
        binding.chart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                if(value<labels.size){
                    return labels.get(value.toInt())
                }else{
                    return value.toString()
                }
            }
        }
    }

    fun updateChart(list: FixedSizeList<WeatherStats>) {
        values = ArrayList()
        labels  = Array(list.size,{""})
        colors  = ArrayList()
        var index: Int = 0;
        if(list.size>=2){
            if(list[list.size-2].aqi-list[list.size-1].aqi<0){
                binding.aqiRate.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_baseline_arrow_upward_24))
            }else{
                binding.aqiRate.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_baseline_arrow_downward_24))
            }
        }
        for (item in list) {
            labels[index] = TimeUtils.getFormatedTimeHelper(item.lastUpdated, "HH:mm a");
            values.add(BarEntry(index++.toFloat(), item.aqi.toFloat()))
            colors.add(ColorUtils.getColorCodeIndex(context!!, item.aqi));
        }
        val set1: BarDataSet;
        if (binding.chart.getData() != null &&
            binding.chart.getData().getDataSetCount() > 0
        ) {
            set1 = binding.chart.getData().getDataSetByIndex(0) as BarDataSet
            set1.values = values
            set1.setColors(colors)
            binding.chart.data = BarData(set1)
            binding.chart.invalidate()
        } else {
            set1 = BarDataSet(values, "Time")
            set1.setColors(colors)
            set1.setDrawIcons(false)
            set1.valueTextSize = 9f
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f
            val data = BarData(set1)
            binding.chart.setData(data)
            binding.chart.notifyDataSetChanged()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}