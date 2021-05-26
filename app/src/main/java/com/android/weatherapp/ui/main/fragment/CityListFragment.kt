package com.android.weatherapp.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.android.weatherapp.R
import com.android.weatherapp.Utils.Status
import com.android.weatherapp.data.model.City
import com.android.weatherapp.databinding.FragmentWeatherListBinding
import com.android.weatherapp.ui.main.adapter.CityListAdapter
import com.android.weatherapp.ui.main.view.MainActivity
import com.android.weatherapp.ui.main.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

// created by Rekha on 5/26/2021

class CityListFragment : Fragment() {

    private lateinit var binding: FragmentWeatherListBinding;
    private lateinit var cityListAdapter: CityListAdapter;
    private lateinit var cities:ArrayList<City>;
    private lateinit var cityIndexMap:HashMap<String,Int>;
    private lateinit var mainViewModel:MainViewModel;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupViewModel()
    }

    fun setupUI(){
        cities = ArrayList()
        cityIndexMap = HashMap();
        Glide.with(context!!).load(R.drawable.loader).into(binding.loadingGif);
        cityListAdapter = CityListAdapter(context = context!!,cities);
        binding.weatherList.layoutManager = LinearLayoutManager(context)
        binding.weatherList.adapter = cityListAdapter
        binding.weatherList.addItemDecoration(
            DividerItemDecoration(
                binding.weatherList.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(
            activity!!
        ).get(MainViewModel::class.java)

        mainViewModel.getCities().observe(this,{
            if (it.status == Status.SUCCESS){
                binding.titleView.visibility = View.VISIBLE
                binding.weatherList.visibility = View.VISIBLE
                binding.loadingGif.visibility = View.GONE
                for(item in it.data!!){
                    if(cityIndexMap.containsKey(item.city)){
                        cities[cityIndexMap.get(item.city)!!] = item
                        cityListAdapter.notifyItemChanged(cityIndexMap.get(item.city)!!)
                    }else{
                        cities.add(item)
                        cityListAdapter.notifyItemInserted(cities.size-1)
                        cityIndexMap.put(item.city,cities.size-1)
                    }
                }
            }else if (it.status == Status.LOADING){
                binding.titleView.visibility = View.GONE
                binding.weatherList.visibility = View.GONE
                binding.loadingGif.visibility = View.VISIBLE
            }
            else if (it.status == Status.ERROR){
                binding.titleView.visibility = View.GONE
                binding.weatherList.visibility = View.GONE
                binding.loadingGif.visibility = View.GONE
                showFailedSnackbar(it.message!!)
            }
        })
    }

    private fun showFailedSnackbar(error :String){
        val snackbar = Snackbar
            .make(binding.root, "Error : $error", Snackbar.LENGTH_INDEFINITE)
            .setAction("Retry") {
                (context as MainActivity).initSocket()
            }

        snackbar.show()
    }
}