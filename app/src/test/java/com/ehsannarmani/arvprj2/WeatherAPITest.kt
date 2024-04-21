package com.ehsannarmani.arvprj2

import androidx.test.platform.app.InstrumentationRegistry
import com.ehsannarmani.arvprj2.repositories.WeatherRepository
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import com.ehsannarmani.arvprj2.models.Response as BaseResponse
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import java.text.SimpleDateFormat
import java.util.TimeZone

val fakeResponse = "<?xml version=\"1.0\" encoding=\"utf-8\"?><meteomatics-api-response version=\"3.0\"><user>cloud_maghari_setare</user><dateGenerated>2024-04-21T06:37:39Z</dateGenerated><status>OK</status><data><parameter name=\"wind_speed_10m:ms\"><location lat=\"35.721324\" lon=\"51.342037\"><value date=\"2024-04-20T13:00:00Z\">2.4</value></location></parameter><parameter name=\"wind_dir_10m:d\"><location lat=\"35.721324\" lon=\"51.342037\"><value date=\"2024-04-20T13:00:00Z\">214.7</value></location></parameter><parameter name=\"t_2m:F\"><location lat=\"35.721324\" lon=\"51.342037\"><value date=\"2024-04-20T13:00:00Z\">77.4</value></location></parameter><parameter name=\"weather_symbol_1h:idx\"><location lat=\"35.721324\" lon=\"51.342037\"><value date=\"2024-04-20T13:00:00Z\">3</value></location></parameter><parameter name=\"precip_24h:mm\"><location lat=\"35.721324\" lon=\"51.342037\"><value date=\"2024-04-20T13:00:00Z\">1.51</value></location></parameter><parameter name=\"uv:idx\"><location lat=\"35.721324\" lon=\"51.342037\"><value date=\"2024-04-20T13:00:00Z\">1</value></location></parameter><parameter name=\"sunset:sql\"><location lat=\"35.721324\" lon=\"51.342037\"><value date=\"2024-04-20T13:00:00Z\">2024-04-20T15:11:00Z</value></location></parameter><parameter name=\"sunrise:sql\"><location lat=\"35.721324\" lon=\"51.342037\"><value date=\"2024-04-20T00:00:00Z\">2024-04-20T01:54:00Z</value></location></parameter><parameter name=\"t_max_2m_24h:C\"><location lat=\"35.721324\" lon=\"51.342037\"><value date=\"2024-04-20T00:00:00Z\">27.6</value></location></parameter><parameter name=\"t_min_2m_24h:C\"><location lat=\"35.721324\" lon=\"51.342037\"><value date=\"2024-04-20T00:00:00Z\">11.7</value></location></parameter></data></meteomatics-api-response>"
class WeatherAPITest {

    private val testDateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    val mockInterceptor = Interceptor {
        Response.Builder()
            .request(Request.Builder().url(WeatherRepository.weatherEndpoint).build())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body(fakeResponse.toResponseBody())
            .build()
    }

    @Test
    fun `call api success and xml parsing correctly`() {

        val client = OkHttpClient.Builder()
            .addInterceptor(mockInterceptor)
            .build()

        val repository = WeatherRepository(client = client)
        val response = repository.getWeather()
        assert(response is BaseResponse.Success)
        val data = response as BaseResponse.Success
        val weather = data.data
        assert(weather.windSpeeds.first() == 2.4)
        assert(weather.windDirections.first() == 214.7)

        val (firstDate,firstTemperature) = weather.temperatures.first()
        assert(firstTemperature == 77.4)
        assert(testDateFormatter.format(firstDate) == "2024-04-20 13:00:00")
        assert(weather.symbols.first() == 3)
        assert(weather.precipitations.first() == 1.51)
        assert(weather.uvs.first() == 1)
        assert(testDateFormatter.format(weather.sunsets.first()) == "2024-04-20 15:11:00")
        assert(testDateFormatter.format(weather.sunrises.first()) == "2024-04-20 01:54:00")
        assert(weather.maxTemperatures.first() == 27.6)
        assert(weather.minTemperatures.first() == 11.7)
    }

    @Test
    fun `call api giving error`(){
        val client = OkHttpClient.Builder()
            .addInterceptor(Interceptor {
                Response.Builder()
                    .code(404)
                    .build()
            })
            .build()

        val repository = WeatherRepository(client = client)
        val response = repository.getWeather()
        assert(response is BaseResponse.Error)
    }
}