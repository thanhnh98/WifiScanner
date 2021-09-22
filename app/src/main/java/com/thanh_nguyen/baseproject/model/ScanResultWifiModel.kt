package com.thanh_nguyen.baseproject.model

import android.net.wifi.ScanResult

data class ScanResultWifiModel(
    val result: ScanResult? = null
): BaseModel()