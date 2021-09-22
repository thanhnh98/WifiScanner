package com.thanh_nguyen.baseproject.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.thanh_nguyen.baseproject.R
import com.thanh_nguyen.baseproject.common.Constants
import com.thanh_nguyen.baseproject.common.base.BaseDialog
import com.thanh_nguyen.baseproject.databinding.DialogInputPasswordBinding
import com.thanh_nguyen.baseproject.model.ScanResultWifiModel
import com.thanh_nguyen.baseproject.utils.onClick

class InputPasswordDialog: BaseDialog<DialogInputPasswordBinding>() {

    companion object {
        fun getInstance(scanResult: ScanResultWifiModel): InputPasswordDialog {
            return InputPasswordDialog().apply {
                arguments = Bundle().apply {
                    putSerializable(Constants.BundleKey.SCAN_RESULT_ITEM, scanResult)
                }
            }
        }
    }

    private var onConfirmPassword: ((String) -> Unit)? = null

    fun setOnInputPasswordListener(action: (String) -> Unit): InputPasswordDialog {
        this.onConfirmPassword = action
        return this
    }

    override fun inflateLayout(): Int = R.layout.dialog_input_password

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val resultScan = arguments?.getSerializable(Constants.BundleKey.SCAN_RESULT_ITEM) as ScanResultWifiModel?
        resultScan?.apply {
            binding.tvWifiName.text = result?.SSID
        }
        binding.btnConnect.onClick {
            onConfirmPassword?.invoke(binding.edtPassword.text.toString())
            dismiss()
        }
        binding.root.onClick {
            //nothing
        }

        binding.edtPassword.doOnTextChanged { text, start, before, count ->
            if (text?.isEmpty() == true){
                binding.btnConnect.isEnabled = false
                binding.btnConnect.background = resources.getDrawable(R.drawable.bg_button_primary_disable, null)
            }
            else {
                binding.btnConnect.isEnabled = true
                binding.btnConnect.background = resources.getDrawable(R.drawable.bg_button_primary_dark, null)
            }
        }
    }
}