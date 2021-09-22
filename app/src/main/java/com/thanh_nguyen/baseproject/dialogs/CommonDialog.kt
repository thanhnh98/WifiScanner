package com.thanh_nguyen.baseproject.dialogs

import com.thanh_nguyen.baseproject.R
import com.thanh_nguyen.baseproject.common.base.BaseDialog
import com.thanh_nguyen.baseproject.databinding.DialogBaseViewBinding

class CommonDialog: BaseDialog<DialogBaseViewBinding>() {
    override fun inflateLayout(): Int = R.layout.dialog_base_view
}