package com.ehsannarmani.arvprj2.repositories

import com.ehsannarmani.arvprj2.models.Weather
import com.ehsannarmani.arvprj2.utils.XMLParser
import com.ehsannarmani.arvprj2.models.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

const val USERNAME = "cloud_maghari_setare"
const val PASSWORD = "0NI7zP87ou"

class WeatherRepository(private val client: OkHttpClient) : IWeatherRepository {

    companion object{
        val weatherDateFormatter =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val utcTime = weatherDateFormatter.apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date())
        val weatherEndpoint =
            "https://api.meteomatics.com/${utcTime}ZP1D:PT1H/wind_speed_10m:ms,wind_dir_10m:d,t_2m:F,weather_symbol_1h:idx,precip_24h:mm,uv:idx,sunset:sql,sunrise:sql,t_max_2m_24h:C,t_min_2m_24h:C/35.721324,51.342037/xml"

    }
    @OptIn(ExperimentalEncodingApi::class)
    override fun getWeather(): Response<Weather> {
        return try {
            val result = client
                .newCall(
                    Request.Builder()
                        .url(weatherEndpoint)
                        .addHeader(
                            "Authorization",
                            "Basic " + Base64.encode("$USERNAME:$PASSWORD".toByteArray())
                        )
                        .get()
                        .build()
                )
                .execute()

            if (result.isSuccessful){
                val weather = XMLParser(stream = result.body!!.byteStream()).parseWeather(dateFormatter = weatherDateFormatter)
                if (weather != null){
                    Response.Success(data = weather)
                }else{
                    Response.Error(message = "Failed to parse XML")
                }
            }else{
                Response.Error(message = result.body?.string().toString())
            }
        }catch (e:Exception){
            Response.Error(message = e.message.toString())
        }
    }
}