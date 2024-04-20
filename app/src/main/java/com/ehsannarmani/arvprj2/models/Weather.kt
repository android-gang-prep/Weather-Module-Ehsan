package com.ehsannarmani.arvprj2.models

import com.ehsannarmani.arvprj2.R
import java.util.Date

data class Weather(
    val windSpeeds:List<Double> = emptyList(),
    val windDirections:List<Double> = emptyList(),
    val temperatures: List<Pair<Date?, Double>> = emptyList(),
    val symbols:List<Int> = emptyList(),
    val precipitations:List<Double> = emptyList(),
    val uvs:List<Int> = emptyList(),
    val sunsets: List<Date?> = emptyList(),
    val sunrises: List<Date?> = emptyList(),
    val maxTemperatures:List<Double> = emptyList(),
    val minTemperatures:List<Double> = emptyList(),
){
    val weatherImage:Int get() = getImageForSymbol(symbols.firstOrNull().toString())
}

fun getImageForSymbol(symbol:String) = when (symbol) {
    "101" -> R.drawable.img_1
    "1" -> R.drawable.img
    "2", "3" -> R.drawable.img_2
    "102", "103" -> R.drawable.img_3
    "11", "12", "4" -> R.drawable.img_4
    "111", "112", "104" -> R.drawable.imm
    "5", "6", "8", "9", "10" -> R.drawable.img_5
    "14", "114" -> R.drawable.img_7
    "13", "113", "7", "107", "115", "15" -> R.drawable.img_8
    "105", "106", "108", "109" -> R.drawable.img_6
    else -> R.drawable.img
}

