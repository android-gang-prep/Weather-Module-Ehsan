package com.ehsannarmani.arvprj2.extensions

fun Double.toCelsius(): Double {
    return (this-32)/1.8
}

fun Double.to1Decimal():String{
    return "%.1f".format(this)
}