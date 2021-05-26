package com.android.weatherapp.ui.main.view

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.android.weatherapp.R
import com.android.weatherapp.Utils.FixedSizeList
import com.android.weatherapp.Utils.Resource
import com.android.weatherapp.Utils.Status
import com.android.weatherapp.data.model.City
import com.android.weatherapp.data.model.WeatherStats
import com.android.weatherapp.databinding.ActivityMainBinding
import com.android.weatherapp.ui.base.MainViewModelFactory
import com.android.weatherapp.ui.main.fragment.CityListFragment
import com.android.weatherapp.ui.main.viewmodel.MainViewModel
import com.android.mainactivity.Utils.Constants
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.json.JSONArray
import java.net.URI
import javax.net.ssl.SSLSocketFactory
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

// created by Rekha on 5/26/2021

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var mainViewModel: MainViewModel
    private lateinit var webSocketClient: WebSocketClient
    private lateinit var binding: ActivityMainBinding;
    private lateinit var cityWeatherStatsMap:HashMap<String,FixedSizeList<WeatherStats>>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(binding.root)
        cityWeatherStatsMap = HashMap()
        setupViewModel()
        loadListFragment()
    }

    override fun onResume() {
        super.onResume()
        initSocket()
    }

    private fun loadListFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, CityListFragment())
        transaction.commit()
    }


    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(
            this,
            MainViewModelFactory()
        ).get(MainViewModel::class.java)

    }

    public fun initSocket(){
        mainViewModel.cities.postValue(Resource(Status.LOADING, ArrayList(), "Fetched Data"))
        webSocketClient = object : WebSocketClient(URI(Constants.SOCKET_URL)) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.i(TAG, "onOpen: ")
            }

            override fun onMessage(message: String?) {
                Log.i(TAG, "onMessage: ")
                var response: JSONArray = JSONArray(message)
                var data: ArrayList<City> = ArrayList();
                for (i in 0 until response.length()) {
                    val city: City = City(
                        response.getJSONObject(i).getString("city"),
                        response.getJSONObject(i).getDouble("aqi"),
                        System.currentTimeMillis()
                    )
                    if(cityWeatherStatsMap.contains(city.city)){
                        val list: FixedSizeList<WeatherStats> = cityWeatherStatsMap.get(city.city)!!
                        list.add(WeatherStats(city.aqi,city.lastUpdated))
                        cityWeatherStatsMap.put(city.city,list)
                    }else {
                        val list:FixedSizeList<WeatherStats> = FixedSizeList(50)
                        list.add(WeatherStats(city.aqi,city.lastUpdated))
                        cityWeatherStatsMap.put(city.city,list)
                    }
                    data.add(city)
                }
                Log.i(TAG, "cityWeatherStatsMap.size : "+cityWeatherStatsMap.size)
                mainViewModel.cityMap.postValue(cityWeatherStatsMap)
                mainViewModel.cities.postValue(Resource(Status.SUCCESS, data, "Fetched Data"))
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.i(TAG, "onClose: "+reason)
            }

            override fun onError(ex: Exception?) {
                Log.i(TAG, "onError: " + ex.toString())
                mainViewModel.cities.postValue(Resource(Status.ERROR, ArrayList(), "Socket Connection Issue"))
            }
        }
        webSocketClient.setSocketFactory(SSLSocketFactory.getDefault())
        webSocketClient.connect()
    }

    override fun onPause() {
        super.onPause()
        if (webSocketClient != null) {
            webSocketClient.close()
        }
    }

}