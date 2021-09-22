package com.thanh_nguyen.baseproject.dialogs

import com.thanh_nguyen.baseproject.R
import com.thanh_nguyen.baseproject.common.base.BaseDialog
import com.thanh_nguyen.baseproject.databinding.DialogConnectWifiRequestBinding

class ConnectWifiDialog: BaseDialog<DialogConnectWifiRequestBinding>() {
    override fun inflateLayout(): Int = R.layout.dialog_connect_wifi_request

}