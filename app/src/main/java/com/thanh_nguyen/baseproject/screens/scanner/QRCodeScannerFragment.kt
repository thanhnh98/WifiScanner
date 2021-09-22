package com.thanh_nguyen.baseproject.screens.scanner

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.zxing.integration.android.IntentIntegrator
import com.thanh_nguyen.baseproject.R
import com.thanh_nguyen.baseproject.common.base.mvvm.fragment.BaseFragment
import com.thanh_nguyen.baseproject.databinding.FragmentQrcodeScannerBinding
import com.thanh_nguyen.baseproject.utils.getWifiScanModel

class QRCodeScannerFragment: BaseFragment<FragmentQrcodeScannerBinding>() {
    override fun inflateLayout(): Int = R.layout.fragment_qrcode_scanner

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        IntentIntegrator.forSupportFragment(this).apply {
            setPrompt("")
            setBeepEnabled(false)
            setOrientationLocked(false)
            setBarcodeImageEnabled(true)
            captureActivity = CustomCaptureView::class.java
        }.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        val resultScanObject = getWifiScanModel(result?.contents)
    }
}