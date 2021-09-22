package com.thanh_nguyen.baseproject.screens.home

import android.Manifest
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.View
import com.thanh_nguyen.baseproject.R
import com.thanh_nguyen.baseproject.common.base.mvvm.fragment.BaseCollectionFragmentMVVM
import com.thanh_nguyen.baseproject.databinding.FragmentHomeBinding
import com.thanh_nguyen.baseproject.dialogs.CommonDialog
import com.thanh_nguyen.baseproject.model.ScanResultWifiModel
import com.thanh_nguyen.baseproject.network.onResultReceived
import com.thanh_nguyen.baseproject.dialogs.InputPasswordDialog
import com.thanh_nguyen.baseproject.screens.home.item.WifiInfoRecycleViewItem
import com.thanh_nguyen.baseproject.utils.*
import com.thanh_nguyen.helper.WifiHelper
import com.thanh_nguyen.permission_manager.PermissionManager
import com.thanh_nguyen.permission_manager.PermissionResult
import io.reactivex.disposables.CompositeDisposable
import kodeinViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment: BaseCollectionFragmentMVVM<FragmentHomeBinding, HomeViewModel>() {

    private val wifiHelper: WifiHelper  = WifiHelper()
    private val wifiManager = wifiHelper.wifiManager

    private var compositeDisposable = CompositeDisposable()

    override fun onViewCreatedX(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedX(view, savedInstanceState)
        checkCurrentWifi()
        setupEvent()

        binding.tvEnableWifi.onClick {
            wifiManager.isWifiEnabled = true
        }

        binding.tvCurrentWifi.onClick {
            CommonDialog().show(activity?.supportFragmentManager?:return@onClick, "TAG")
        }

        wifiHelper.registerWifiProgressReceiver(activity?:return)
    }

    private fun checkCurrentWifi(){
        when(wifiManager.wifiState){
            WifiManager.WIFI_STATE_DISABLED -> { //DISABLED
                colorTransform(R.color.color_enable, R.color.color_disable){
                    binding.bgHeader.setBackgroundColor(it)
                }
                colorTransform(R.color.white, R.color.black){
                    binding.tvCurrentWifi.setTextColor(it)
                }
                hideListWifiScan()
                binding.apply {
                    groupEnableWifi.visibility = View.VISIBLE
                    tvCurrentWifi.visibility = View.GONE
                }
            }

            WifiManager.WIFI_STATE_ENABLED -> { //ENABLE
                val currentWifi = wifiManager.connectionInfo
                binding.tvCurrentWifi.text = "Đã kết nối đến " + currentWifi.ssid.filter { it != '"' }
                colorTransform(R.color.color_disable, R.color.color_enable){
                    binding.bgHeader.setBackgroundColor(it)
                }
                colorTransform(R.color.black, R.color.white){
                    binding.tvCurrentWifi.setTextColor(it)
                }
                binding.apply {
                    groupEnableWifi.visibility = View.GONE
                    tvCurrentWifi.visibility = View.VISIBLE
                }
                refreshWifiState()
            }
        }
    }

    private fun setupEvent() {
        observeLiveDataChanged(wifiHelper.resultScan){
            it.onResultReceived(
                onLoading = {
                    showLoading()
                },
                onSuccess = { result ->
                    hideLoading()
                    showListWifiScan(result.data?:return@observeLiveDataChanged)
                    wifiHelper.unregisterReceiver(activity?:return@observeLiveDataChanged)
                },
                onError = {
                    activity?.showMessage(it.message?:"Failed")
                },
            )
        }

        compositeDisposable.add(
            wifiHelper
                .onWifiChangedListener()
                .subscribe {
                    checkCurrentWifi()
                }
        )
    }
    private fun scanWifi(){
        wifiHelper.registerReceiver(activity?:return)
        wifiHelper.scanWifi(true)
    }

    private fun refreshWifiState() {
        if (hasPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )) {
            scanWifi()
        }
        else{
            GlobalScope.launch {
                val result = PermissionManager.requestPermissions(
                    this@HomeFragment,
                    999,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )

                result.apply {
                    when(this){
                        is PermissionResult.PermissionGranted -> {
                            scanWifi()
                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }

    override fun initClusters() {
        addCluster(WifiInfoRecycleViewItem::class.java)
    }

    override val viewModel: HomeViewModel by kodeinViewModel()

    override fun inflateLayout(): Int = R.layout.fragment_home

    override fun onRefresh() {
        super.onRefresh()
        scanWifi()
    }

    private fun showListWifiScan(listResults: List<ScanResult>){
        val list = listResults.map {
            ScanResultWifiModel(it)
        }
        recyclerManager.replace(
            WifiInfoRecycleViewItem::class.java,
            list
                .filter {
                    !it.result?.SSID.isNullOrEmpty()
                }
                .map {
                    WifiInfoRecycleViewItem(
                        it,
                        isConnected = wifiHelper.isSameCurrentNetwork(it.result?.SSID)
                    ){ result ->
                        requestToConnectWifi(result)
                    }
                }
        )
    }

    private fun hideListWifiScan(){
        recyclerManager.replace(
            WifiInfoRecycleViewItem::class.java,
            ArrayList()
        )
    }

    private fun requestToConnectWifi(result: ScanResultWifiModel) {
        if (wifiHelper.isSameCurrentNetwork(result.result?.SSID))
            return

        if (isOpenWifi(result.result?.capabilities?:""))
            connectToWifi(result.result?.SSID?:return, "")
        else
            connectToWifi(result.result?.SSID?:return, "0389048282a")
//
//        InputPasswordDialog.getInstance(result)
//                .setOnInputPasswordListener {
//                    connectToWifi(result.result?.SSID?:return@setOnInputPasswordListener, it)
//                }
//                .show(activity?.supportFragmentManager?:return, "TAG")
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}