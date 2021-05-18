package com.example.aqiweatherapp.ui.main.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.aqiweatherapp.R
import com.example.aqiweatherapp.Utils.Resource
import com.example.aqiweatherapp.Utils.Status
import com.example.aqiweatherapp.data.model.City
import com.example.aqiweatherapp.databinding.ActivityMainBinding
import com.example.aqiweatherapp.ui.base.MainViewModelFactory
import com.example.aqiweatherapp.ui.main.adapter.CityListAdapter
import com.example.aqiweatherapp.ui.main.fragment.WeatherListFragment
import com.example.aqiweatherapp.ui.main.viewmodel.MainViewModel
import com.example.mainactivity.Utils.Constants
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.json.JSONArray
import java.net.URI
import javax.net.ssl.SSLSocketFactory


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var mainViewModel: MainViewModel
    private lateinit var webSocketClient: WebSocketClient
    private lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(binding.root)
        setupViewModel()
        loadListFragment()
    }

    private fun loadListFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, WeatherListFragment())
        transaction.commit()
    }


    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(
            this,
            MainViewModelFactory()
        ).get(MainViewModel::class.java)

    }

    override fun onResume() {
        super.onResume()
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
                    data.add(city)
                }
                mainViewModel.cities.postValue(Resource(Status.SUCCESS, data, "Fetched Data"))
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.i(TAG, "onClose: "+reason)
            }

            override fun onError(ex: Exception?) {
                Log.i(TAG, "onError: " + ex.toString())
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