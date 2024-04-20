package com.ehsannarmani.arvprj2.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ehsannarmani.arvprj2.models.Weather
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


class HomeViewModel: ViewModel() {

    private val _response = MutableStateFlow<Response<Weather>>(Response.Loading)
    val response = _response.asStateFlow()

    private val client = OkHttpClient.Builder()
        .readTimeout(1,TimeUnit.MINUTES)
        .writeTimeout(1,TimeUnit.MINUTES)
        .connectTimeout(1,TimeUnit.MINUTES)
        .callTimeout(1,TimeUnit.MINUTES)
        .build()


    init {
        getWeather()
    }
    @OptIn(ExperimentalEncodingApi::class)
    fun getWeather(){
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val utcTime = formatter.apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date())
        viewModelScope.launch(Dispatchers.IO){
            client
                .newCall(Request.Builder()
                    .url("https://api.meteomatics.com/${utcTime}ZP1D:PT1H/wind_speed_10m:ms,wind_dir_10m:d,t_2m:F,weather_symbol_1h:idx,precip_24h:mm,uv:idx,sunset:sql,sunrise:sql,t_max_2m_24h:C,t_min_2m_24h:C/35.721324,51.342037/xml")
                    .addHeader("Authorization","Basic "+ Base64.encode("cloud_maghari_setare:0NI7zP87ou".toByteArray()))
                    .get()
                    .build())
                .enqueue(object :Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        _response.update { Response.Error(e.message.toString()) }
                    }

                    override fun onResponse(call: Call, response: okhttp3.Response) {
                        if (response.isSuccessful){
                            val documentFactory = DocumentBuilderFactory.newInstance()
                            val docBuilder = documentFactory.newDocumentBuilder()

                            val doc = docBuilder.parse(response.body?.byteStream())
                            var weather = Weather()


                            doc.getElementsByTagName("parameter")
                                .forEach {
                                    val namedItem = it.attributes.getNamedItem("name")
                                    with(namedItem.nodeValue){
                                        val locationTag = it.childNodes.item(0)
                                        val values = locationTag.childNodes.values()
                                        if (startsWith("wind_speed")){
                                            weather = weather.copy(windSpeeds = values.map { it.toDouble() })
                                        }else if (startsWith("wind_dir")){
                                            weather = weather.copy(windDirections = values.map { it.toDouble() })
                                        }else if (startsWith("t_2m")){
                                            weather = weather.copy(temperatures = locationTag.childNodes.valuesWithDate(formatter).map { mapper-> mapper.first to mapper.second.toDouble() })
                                        }else if (startsWith("weather_symbol_1h")){
                                            weather = weather.copy(symbols = values.map { it.toInt() })
                                        }else if (startsWith("precip_24h")){
                                            weather = weather.copy(precipitations = values.map { it.toDouble() })
                                        }else if (startsWith("uv:idx")){
                                            weather = weather.copy(uvs = values.map { it.toInt() })
                                        }else if (startsWith("sunset:sql")){
                                            weather = weather.copy(sunsets = values.map { formatter.parse(it) })
                                        }else if (startsWith("sunrise:sql")){
                                            weather = weather.copy(sunrises = values.map { formatter.parse(it) })
                                        }else if (startsWith("t_max_2m_24h")){
                                            weather = weather.copy(maxTemperatures = values.map { it.toDouble() })
                                        }else if (startsWith("t_min_2m_24h")){
                                            weather = weather.copy(minTemperatures = values.map { it.toDouble() })
                                        }
                                    }
                                }
                            _response.update { Response.Success(data = weather) }
                        }else {
                            _response.update { Response.Error(message = response.body?.string().toString()) }
                        }
                    }

                })
        }
    }

}

fun NodeList.valuesWithDate(formatter:SimpleDateFormat):List<Pair<Date?,String>>{
    val result = mutableListOf<Pair<Date?,String>>()
    for (i in 0 until length){
        val date = formatter.parse(item(i).attributes.getNamedItem("date").nodeValue.orEmpty())
        result.add(date to item(i).textContent)
    }
    return result
}
fun NodeList.values():List<String>{
    val result = mutableListOf<String>()
    for (i in 0 until length){
        result.add(item(i).textContent)
    }
    return result
}

fun NodeList.forEach(onEach:(Node)->Unit){
    for (i in 0 until length){
        onEach(item(i))
    }
}

sealed class Response<out T>{
    data class Success<T>(val data:T):Response<T>()
    data class Error(val message:String):Response<Nothing>()
    data object Loading:Response<Nothing>()
}