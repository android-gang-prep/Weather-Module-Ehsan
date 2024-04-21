package com.ehsannarmani.arvprj2.repositories

import com.ehsannarmani.arvprj2.models.Weather
import com.ehsannarmani.arvprj2.models.Response
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient

interface IWeatherRepository {
    fun getWeather():Response<Weather>
}