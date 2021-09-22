package com.thanh_nguyen.baseproject

import com.thanh_nguyen.baseproject.utils.WifiScanModelData
import com.thanh_nguyen.baseproject.utils.getWifiScanModel
import com.thanh_nguyen.baseproject.utils.toJson
import com.thanh_nguyen.baseproject.utils.trimLastChar
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testGetWifiScanModel(){
        val resultScan = "WIFI:S:Thanh 5G;T:WPA;P:0389048282a;H:false;;".trimLastChar(';')
        print(getWifiScanModel(resultScan).toJson())
    }
}