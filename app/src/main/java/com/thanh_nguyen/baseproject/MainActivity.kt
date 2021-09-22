package com.thanh_nguyen.baseproject

import android.os.Bundle
import com.thanh_nguyen.baseproject.common.base.mvvm.activity.BaseActivity
import com.thanh_nguyen.baseproject.databinding.ActivityMainBinding
import com.thanh_nguyen.baseproject.screens.home.HomeFragment

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun inflateLayout(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, HomeFragment())
            .commit()
    }

    override fun onBackPressed() {
        finish()
    }
}