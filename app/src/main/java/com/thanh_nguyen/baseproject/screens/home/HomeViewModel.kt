package com.thanh_nguyen.baseproject.screens.home

import android.net.wifi.ScanResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.thanh_nguyen.baseproject.common.base.mvvm.viewmodel.BaseCollectionViewModel
import com.thanh_nguyen.baseproject.common.event.SingleLiveEvent
import com.thanh_nguyen.baseproject.network.Result
import kotlinx.coroutines.launch

class HomeViewModel: BaseCollectionViewModel() {
    private val _listWifiResult = SingleLiveEvent<Result<List<ScanResult>>>()
    val listWifiResult: LiveData<Result<List<ScanResult>>> get() = _listWifiResult

    fun onScanWifiSuccess(list: List<ScanResult>){
        viewModelScope.launch {
            _listWifiResult.postValue(Result.success(list))
        }
    }

    fun onScanWifiFailed(){
        viewModelScope.launch {
            _listWifiResult.postValue(Result.error("WTF"))
        }
    }
}