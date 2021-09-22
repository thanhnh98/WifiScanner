package com.thanh_nguyen.baseproject.utils

import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import com.thanh_nguyen.baseproject.R
import com.thanh_nguyen.baseproject.app.AppApplication
import com.thanh_nguyen.baseproject.model.ScanResultWifiModel
import com.thanh_nguyen.baseproject.model.SignalLevel

@BindingAdapter("setWifiSignalIcon")
fun setWifiSignalIcon(imageView: AppCompatImageView, data: ScanResultWifiModel){
    val level = getSignalLevel(data.result?.level?:-1000)

    when(level){
        is SignalLevel.BestSignal -> {
            imageView.setImageDrawable(AppApplication.getContext().getDrawable(R.drawable.ic_wifi_best))
        }

        is SignalLevel.GoodSignal -> {
            imageView.setImageDrawable(AppApplication.getContext().getDrawable(R.drawable.ic_wifi_normal))
        }

        is SignalLevel.LowSignal -> {
            imageView.setImageDrawable(AppApplication.getContext().getDrawable(R.drawable.ic_wifi_week))
        }

        is SignalLevel.WeakSignal -> {
            imageView.setImageDrawable(AppApplication.getContext().getDrawable(R.drawable.ic_wifi_week))
        }

        else -> {
            imageView.setImageDrawable(AppApplication.getContext().getDrawable(R.drawable.ic_wifi_too_low))
        }
    }
}