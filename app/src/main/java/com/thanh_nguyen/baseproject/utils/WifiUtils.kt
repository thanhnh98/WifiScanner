package com.thanh_nguyen.baseproject.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.thanh_nguyen.baseproject.app.AppApplication


fun getSecurityTypeScheme(capabilities: String): String{
    var type = ""
    if (capabilities.contains("WPA"))
        type += "WPA/"
    if (capabilities.contains("WPA2"))
        type += "WPA2/"
    if (capabilities.contains("WEP"))
        type += "WEP/"

    return if (type.isEmpty())
        "OPEN"
    else
        type.substring(0, type.length - 1)
}

fun isOpenWifi(capabilities: String): Boolean{
    if (capabilities.contains("WPA"))
        return false
    if (capabilities.contains("WPA2"))
        return false
    if (capabilities.contains("WEP"))
        return false

    return true
}


private fun isWifiConnected (wifiManager: WifiManager, machineID: String) : Boolean{
    if (wifiManager.isWifiEnabled) {
        val wifiInfo = wifiManager.connectionInfo
        if (wifiInfo?.ssid == machineID)
            return true
    }

    return false
}

fun connectToWifi(ssid: String, key: String) {
    val wifiManager = AppApplication.getContext().applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
            Log.e("connect to", "$ssid")
            // Remove possible conflicting network suggestions as we're not allowed to edit them
            wifiManager.removeNetworkSuggestions(
                listOf(
                    WifiNetworkSuggestion.Builder()
                        .setSsid(ssid)
                        .build()
                )
            )

            // Add new network suggestion
            val status = wifiManager.addNetworkSuggestions(
                listOf(
                    WifiNetworkSuggestion.Builder()
                        .setSsid(ssid)
                        .setWpa2Passphrase(key)
                        .setIsAppInteractionRequired(true)
                        .build()
                )
            )
            if (status == WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
                Log.e("result success", "Success")
            }

            val intentFilter =
                IntentFilter(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION)

            val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (intent.action != WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION) {
                        return
                    }
                    Log.e("ok ne","??")
                    // Post connection
                }
            }
            AppApplication.getContext().applicationContext.registerReceiver(broadcastReceiver, intentFilter)
        }
        else -> {
            Log.e("login with","$ssid - $key")

            val wifiConfig = WifiConfiguration()

            wifiConfig.SSID = "\"" + ssid + "\""
            wifiConfig.preSharedKey = "\"" + key + "\""
            //wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
            //wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40)

            val netId = wifiManager.addNetwork(wifiConfig)
            wifiManager.disconnect()
            wifiManager.enableNetwork(netId, true)
            wifiManager.reconnect()
        }
    }
}