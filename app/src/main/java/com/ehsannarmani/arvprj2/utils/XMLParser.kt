package com.ehsannarmani.arvprj2.utils

import com.ehsannarmani.arvprj2.models.Weather
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import javax.xml.parsers.DocumentBuilderFactory

class XMLParser(
    private val stream: InputStream
) {

    fun parseWeather(
        dateFormatter:SimpleDateFormat,
    ):Weather?{
        return try {
            val documentFactory = DocumentBuilderFactory.newInstance()
            val docBuilder = documentFactory.newDocumentBuilder()

            val doc = docBuilder.parse(stream)
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
                            weather = weather.copy(temperatures = locationTag.childNodes.valuesWithDate(dateFormatter).map { mapper-> mapper.first to mapper.second.toDouble() })
                        }else if (startsWith("weather_symbol_1h")){
                            weather = weather.copy(symbols = values.map { it.toInt() })
                        }else if (startsWith("precip_24h")){
                            weather = weather.copy(precipitations = values.map { it.toDouble() })
                        }else if (startsWith("uv:idx")){
                            weather = weather.copy(uvs = values.map { it.toInt() })
                        }else if (startsWith("sunset:sql")){
                            weather = weather.copy(sunsets = values.map { dateFormatter.parse(it) })
                        }else if (startsWith("sunrise:sql")){
                            weather = weather.copy(sunrises = values.map { dateFormatter.parse(it) })
                        }else if (startsWith("t_max_2m_24h")){
                            weather = weather.copy(maxTemperatures = values.map { it.toDouble() })
                        }else if (startsWith("t_min_2m_24h")){
                            weather = weather.copy(minTemperatures = values.map { it.toDouble() })
                        }
                    }
                }
            weather
        }catch (e:Exception){
            null
        }
    }
    private fun NodeList.valuesWithDate(formatter:SimpleDateFormat):List<Pair<Date?,String>>{
        val result = mutableListOf<Pair<Date?,String>>()
        for (i in 0 until length){
            val date = formatter.parse(item(i).attributes.getNamedItem("date").nodeValue.orEmpty())
            result.add(date to item(i).textContent)
        }
        return result
    }
    private fun NodeList.values():List<String>{
        val result = mutableListOf<String>()
        for (i in 0 until length){
            result.add(item(i).textContent)
        }
        return result
    }

    private fun NodeList.forEach(onEach:(Node)->Unit){
        for (i in 0 until length){
            onEach(item(i))
        }
    }
}