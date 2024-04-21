package com.ehsannarmani.arvprj2.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ehsannarmani.arvprj2.models.Response
import com.ehsannarmani.arvprj2.models.Weather
import com.ehsannarmani.arvprj2.repositories.IWeatherRepository
import com.ehsannarmani.arvprj2.repositories.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

 val client = OkHttpClient.Builder()
    .readTimeout(1,TimeUnit.MINUTES)
    .writeTimeout(1,TimeUnit.MINUTES)
    .connectTimeout(1,TimeUnit.MINUTES)
    .callTimeout(1,TimeUnit.MINUTES)
    .build()


class HomeViewModel: ViewModel() {

    private val _response = MutableStateFlow<Response<Weather>>(Response.Loading)
    val response = _response.asStateFlow()


    private val repository:IWeatherRepository = WeatherRepository(client)


    init {
        getWeather()
    }
    private fun getWeather(){
        viewModelScope.launch(Dispatchers.IO){
            _response.update { repository.getWeather() }
        }
    }
}
