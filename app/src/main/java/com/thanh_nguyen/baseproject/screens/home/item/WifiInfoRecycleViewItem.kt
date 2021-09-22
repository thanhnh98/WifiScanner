package com.thanh_nguyen.baseproject.screens.home.item

import android.graphics.Typeface
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.thanh_nguyen.baseproject.R
import com.thanh_nguyen.baseproject.app.AppApplication
import com.thanh_nguyen.baseproject.common.base.adapter.BindingRecycleViewItem
import com.thanh_nguyen.baseproject.databinding.ItemWifiInfoBinding
import com.thanh_nguyen.baseproject.model.ScanResultWifiModel
import com.thanh_nguyen.baseproject.model.SignalLevel
import com.thanh_nguyen.baseproject.utils.*

class WifiInfoRecycleViewItem(
        private val scanResult: ScanResultWifiModel?,
        private val isConnected: Boolean ? = false,
        private val onItemClick: (ScanResultWifiModel) -> Unit,
    ): BindingRecycleViewItem<ItemWifiInfoBinding, WifiInfoVH>() {
    override fun inflateViewHolder(parent: ViewGroup): WifiInfoVH {
        return WifiInfoVH(
            inflateView(
                parent,
                R.layout.item_wifi_info
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun bindModel(binding: ItemWifiInfoBinding?, viewHolder: WifiInfoVH) {
        binding?.scanData = scanResult
        binding?.tvIsConnected?.visibility = if (isConnected == true) View.VISIBLE else View.GONE

        binding?.tvWifiName?.typeface = if (isConnected == true)
             Typeface.create("sans-serif", Typeface.BOLD)

        else
            Typeface.create("sans-serif", Typeface.NORMAL)

        val secType = getSecurityTypeScheme(scanResult?.result?.capabilities?:"")

        if (secType == "OPEN")
            binding?.imgLock?.visibility = View.GONE
        else
            binding?.imgLock?.visibility = View.VISIBLE

        binding?.tvType?.text = secType

        if (isConnected == true) {
            binding?.imgLevel?.setBackgroundTint(
                getColorByLevel(getSignalLevel(scanResult?.result?.level ?: 999))
            )
        }
        else {
            binding?.imgLevel?.setBackgroundTint(
                AppApplication.getContext().resources.getColor(R.color.color_wifi_disable, null)
            )
        }

        binding?.root?.onClick {
            scanResult?.apply(onItemClick)
        }
    }
}