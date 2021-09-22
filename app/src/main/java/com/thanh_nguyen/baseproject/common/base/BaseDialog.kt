package com.thanh_nguyen.baseproject.common.base

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.thanh_nguyen.baseproject.utils.onClick
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

abstract class BaseDialog<DB: ViewDataBinding>: DialogFragment(), KodeinAware {
    open lateinit var binding: DB

    override val kodein by kodein()
    private var mIsShown = false

    @LayoutRes
    abstract fun inflateLayout(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, inflateLayout(), container,false)
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.root.onClick {
            dismiss()
        }
        return binding.root
    }

    override fun onStart() {
        val dialog = dialog
        if (dialog != null) {
            setWidthPercent(80)
        }
        super.onStart()
    }

    fun DialogFragment.setWidthPercent(percentage: Int) {
        val percent = percentage.toFloat() / 100
        val dm = Resources.getSystem().displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * percent
        dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    /**
     * Call this method (in onActivityCreated or later)
     * to make the dialog near-full screen.
     */
    fun DialogFragment.setFullScreen() {
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!mIsShown) {
            mIsShown = true
            super.show(manager, tag)
        }
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        return if (mIsShown) {
            -1
        } else {
            mIsShown = true
            super.show(transaction, tag)
        }
    }
}