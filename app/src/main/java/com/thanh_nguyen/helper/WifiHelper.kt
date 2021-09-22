package com.thanh_nguyen.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.thanh_nguyen.baseproject.app.AppApplication
import com.thanh_nguyen.baseproject.network.Result
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class WifiHelper {
    var wifiManager = AppApplication.getContext().applicationContext.getSystemService(
        Context.WIFI_SERVICE) as WifiManager
    private val intentFilter = IntentFilter().apply {
        addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
    }

    private val intentFilterWifiProcess = IntentFilter().apply {
        addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
    }

    private var _resultScan = MutableLiveData<Result<List<ScanResult>>>()
    val resultScan: LiveData<Result<List<ScanResult>>> get() = _resultScan

    fun registerReceiver(context: Context){
        context.registerReceiver(wifiScanReceiver, intentFilter)
    }

    fun unregisterReceiver(context: Context){
        context.unregisterReceiver(wifiScanReceiver)
    }

    fun registerWifiProgressReceiver(context: Context){
        context.registerReceiver(wifiProcessReceiver, intentFilterWifiProcess)
    }

    fun unregisterWifiProgressReceiver(context: Context){
        context.unregisterReceiver(wifiProcessReceiver)
    }

    fun scanWifi(isShowLoading: Boolean = false){
        if (isShowLoading)
            _resultScan.postValue(Result.loading(null))
        wifiManager.startScan()
    }

    fun isSameCurrentNetwork(ssid: String?): Boolean{
        if (ssid == null)
            return false

        val ssid1 = ssid.filter { it != '"' }
        val ssid2 = wifiManager.connectionInfo.ssid.filter { it != '"' }

        return ssid1 == ssid2
    }

    private val wifiScanReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceive(context: Context, intent: Intent) {
            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            if (success) {
                val results = wifiManager.scanResults
                onScanSuccess(results)
            } else {
                scanFailure()
            }
        }
    }

    private val wifiProcessReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val extraWifiState = intent?.getIntExtra(
                WifiManager.EXTRA_WIFI_STATE,
                WifiManager.WIFI_STATE_UNKNOWN
            )
            when(extraWifiState) {
                WifiManager.WIFI_STATE_DISABLED -> {
                    Log.e("status","disable")
                }
                WifiManager.WIFI_STATE_ENABLED -> {
                    Log.e("status","eenable")
                }
                WifiManager.WIFI_STATE_ENABLING -> {
                    Log.e("status","enabling")
                }
                WifiManager.WIFI_STATE_DISABLING -> {
                    Log.e("status","disabling")
                }
                WifiManager.WIFI_STATE_UNKNOWN -> {
                    Log.e("status","unknown")
                }
            }
        }

    }

    private fun scanFailure() {
        _resultScan.postValue(Result.error("Scan failed"))
    }

    private fun onScanSuccess(results: List<ScanResult>) {
        _resultScan.postValue(Result.success(results))
    }

    fun onWifiChangedListener(): Observable<Connectivity> =
        ReactiveNetwork
            .observeNetworkConnectivity(AppApplication.getContext())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}