package com.thanh_nguyen.baseproject.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.google.gson.Gson
import com.thanh_nguyen.baseproject.R
import com.thanh_nguyen.baseproject.app.AppApplication
import com.thanh_nguyen.baseproject.model.SignalLevel
import java.text.DecimalFormat
import java.util.*

fun Any.toJson(): String{
    return Gson().toJson(this)
}

fun Context.hasPermissions(vararg permissions: String) = permissions.all { permission ->
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Context.copyToClipboard(label: String = "copied text", content: String) {
    val clipboardManager = ContextCompat.getSystemService(this, ClipboardManager::class.java)!!
    val clip = ClipData.newPlainText(label, content)
    clipboardManager.setPrimaryClip(clip)
}

fun Date.plusDays(days: Int): Date {
    return Date(this.time + (days * 24 * 60 * 60 * 1000))
}

fun Date.plusMillis(millis: Long): Date {
    return Date(this.time + millis)
}

fun Any?.isNull() = this == null

fun Double.formatPrice(pattern: String? = "###,###,###.00"): String = DecimalFormat(pattern).format(this)

fun <VB: ViewDataBinding> inflateDataBinding(parent: ViewGroup, @LayoutRes layoutRes: Int, attachParent: Boolean = false): VB{
    return DataBindingUtil.inflate(
        LayoutInflater.from(parent.context),
        layoutRes,
        parent,
        attachParent
    )
}

fun inflateView(parent: ViewGroup, @LayoutRes layoutRes: Int): View{
    return LayoutInflater
        .from(parent.context)
        .inflate(layoutRes, null)
}

fun View.onClick(f: () -> Unit){
    setOnClickListener {
        f.invoke()
    }
}

fun Activity.showMessage(msg: String){
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

inline fun <reified T, reified LD: LiveData<T>> Fragment.observeLiveDataChanged(liveData: LD, crossinline onChanged: (T) -> Unit){
    liveData.observe(viewLifecycleOwner, onChanged)
}

inline fun <reified T, reified LD: LiveData<T>> AppCompatActivity.observeLiveDataChanged(liveData: LD, observer: Observer<in T>){
    liveData.observe(this, observer)
}

fun getSignalLevel(level: Int): SignalLevel {
    return when {
        level >= -50 -> {
            //Best signal
            SignalLevel.BestSignal(level)
        }
        level >= -70 -> {
            //Good signal
            SignalLevel.GoodSignal(level)
        }
        level >= -80 -> {
            //Low signal
            SignalLevel.LowSignal(level)
        }
        level >= -100 -> {
            //Very weak signal
            SignalLevel.WeakSignal(level)
        }
        else -> {
            //Too low signal
            SignalLevel.TooLowSignal(level)
        }
    }
}

fun getColorByLevel(signal: SignalLevel): Int{
    return when(signal){

        is SignalLevel.BestSignal -> {
            AppApplication.getContext().resources.getColor(R.color.color_wifi_best, null)
        }

        is SignalLevel.GoodSignal -> {
            AppApplication.getContext().resources.getColor(R.color.color_wifi_normal, null)

        }

        is SignalLevel.LowSignal -> {
            AppApplication.getContext().resources.getColor(R.color.color_wifi_week, null)
        }

        is SignalLevel.WeakSignal -> {
            AppApplication.getContext().resources.getColor(R.color.color_wifi_too_low, null)
        }

        else -> {
            AppApplication.getContext().resources.getColor(R.color.color_wifi_too_low, null)
        }
    }
}

fun AppCompatActivity.addFragment(fragment: Fragment, @IdRes id: Int){
    supportFragmentManager.beginTransaction()
        .replace(id, fragment)
        .commit()
}

fun AppCompatActivity.showMessage(msg: String){
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.hasPermissions(vararg listPermissions: String): Boolean{
    listPermissions.forEach {
        if (checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED)
            return false
    }
    return true
}

fun AppCompatActivity.hasSinglePermission(permission: String): Boolean{
    return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
}

fun Fragment.hasPermissions(vararg listPermissions: String): Boolean{
    listPermissions.forEach {
        if (activity?.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED)
            return false
    }
    return true
}

fun Fragment.hasSinglePermission(permission: String): Boolean{
    return activity?.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
}

fun AppCompatImageView.setBackgroundTint(color: Int){
    setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
}