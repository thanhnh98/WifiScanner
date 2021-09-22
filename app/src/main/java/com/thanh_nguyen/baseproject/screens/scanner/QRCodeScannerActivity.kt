package com.thanh_nguyen.baseproject.screens.scanner

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.thanh_nguyen.baseproject.R
import com.thanh_nguyen.baseproject.common.base.mvvm.activity.BaseActivity
import com.thanh_nguyen.baseproject.databinding.ActivityQrcodeScannerBinding
import com.thanh_nguyen.baseproject.utils.addFragment


class QRCodeScannerActivity: BaseActivity<ActivityQrcodeScannerBinding>() {
    override fun inflateLayout(): Int = R.layout.activity_qrcode_scanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addFragment(QRCodeScannerFragment(), R.id.container)

    }

    override fun onBackPressed() {
        finish()
    }
}