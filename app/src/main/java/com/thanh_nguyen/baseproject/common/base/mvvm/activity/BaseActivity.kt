package com.thanh_nguyen.baseproject.common.base.mvvm.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.thanh_nguyen.baseproject.R
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

@Suppress("DEPRECATION")
abstract class BaseActivity<DB: ViewDataBinding>: AppCompatActivity(), KodeinAware {
    open lateinit var binding: DB

    override val kodein by kodein()

    @LayoutRes
    abstract fun inflateLayout(): Int

    open fun inflateStatusColor() = R.color.colorPrimaryDark

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, inflateLayout())
        hideSystemUI()
        setStatusBarColor()
    }

    private fun setStatusBarColor(@ColorRes colorRes: Int = inflateStatusColor()) {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = resources.getColor(colorRes)
        }
    }

    /**
    * If increase version code to 30 (R) , remove brackets
     * */
    private fun hideSystemUI(){
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
    }
}