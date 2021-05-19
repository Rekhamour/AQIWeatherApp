package com.example.aqiweatherapp.ui.main.fragment

import android.graphics.DashPathEffect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.aqiweatherapp.Utils.ColorUtils
import com.example.aqiweatherapp.Utils.LimitedSizeQueue
import com.example.aqiweatherapp.Utils.TimeUtils
import com.example.aqiweatherapp.data.model.Stats
import com.example.aqiweatherapp.databinding.FragmentGraphBinding
import com.example.aqiweatherapp.ui.main.viewmodel.MainViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter


class GraphFragment : Fragment() {

    private lateinit var binding: FragmentGraphBinding;
    private lateinit var mainViewModel: MainViewModel;
    private lateinit var values: ArrayList<BarEntry>;
    private lateinit var labels: Array<String>
    private lateinit var colors: ArrayList<Int>
    private val TAG = "GraphFragment"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

    public fun setUpUI() {
        binding.backBtn.setOnClickListener {
            fragmentManager!!.popBackStackImmediate()
        }
        binding.cityName.text = this.arguments!!.getString("CITY").toString()
    }

    public fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(
            activity!!
        ).get(MainViewModel::class.java)

        mainViewModel.cityMap.observe(this, {
            if (it.containsKey(binding.cityName.text)) {
                Log.i(TAG, "setupViewModel: " + it.get(binding.cityName.text)!!)

                updateChart(it.get(binding.cityName.text)!!);
            }
        })
    }

    private fun initChart(){
        binding.chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.chart.xAxis.setGranularity(1f);
        binding.chart.xAxis.setGranularityEnabled(true);
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

    fun updateChart(list: LimitedSizeQueue<Stats>) {
        values = ArrayList()
        labels  = Array(list.size,{""})
        colors  = ArrayList()
        var index: Int = 0;


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
            GraphFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}