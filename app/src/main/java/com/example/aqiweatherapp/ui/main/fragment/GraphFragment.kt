package com.example.aqiweatherapp.ui.main.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aqiweatherapp.R
import com.example.aqiweatherapp.databinding.FragmentGraphBinding
import com.example.aqiweatherapp.databinding.FragmentWeatherListBinding

class GraphFragment : Fragment() {

    private lateinit var binding:FragmentGraphBinding;

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
        binding.backBtn.setOnClickListener {
            fragmentManager!!.popBackStackImmediate()

        }
        binding.cityName.text = this.arguments!!.getString("CITY").toString()
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