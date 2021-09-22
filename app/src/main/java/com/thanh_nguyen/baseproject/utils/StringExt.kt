package com.thanh_nguyen.baseproject.utils

import org.jetbrains.annotations.TestOnly

//WIFI:S:Thanh 5G;T:WPA;P:0389048282a;H:false;;
fun getWifiScanModel(resultScan: String?): WifiScanModelData{
    if (resultScan == null)
        return WifiScanModelData()

    val arr = resultScan.split(";")
    var ssid: String? = null
    var type: String? = null
    var password: String? = null
    var h: String? = null

    if (arr.isNotEmpty())
        ssid = arr[0].split(":").last()

    if (arr.size > 2) {
        type = arr[1].split(":").last()
        password = arr[2].split(":").last()
    }

    if (arr.size > 3)
        h = arr[3].split(":").last()

    return WifiScanModelData(
        ssid = ssid,
        password = password,
        type = type,
        h = h
    )
}

fun String.trimLastChar(c: Char): String{
    return this.trimEnd {
        it == c
    }
}

data class WifiScanModelData(
    val ssid: String? = null,
    val password: String? = null,
    val type: String? = null,
    val h: String? = null
)