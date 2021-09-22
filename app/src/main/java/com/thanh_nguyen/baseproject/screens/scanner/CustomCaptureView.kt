package com.thanh_nguyen.baseproject.screens.scanner

import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.thanh_nguyen.baseproject.R

class CustomCaptureView: CaptureActivity() {
    override fun initializeContent(): DecoratedBarcodeView {
        setContentView(R.layout.view_capture_view)
        return findViewById(R.id.dbv_capture_view)
    }
}