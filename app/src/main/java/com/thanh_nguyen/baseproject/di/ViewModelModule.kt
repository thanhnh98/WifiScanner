package com.thanh_nguyen.baseproject.di

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import bindViewModel
import com.thanh_nguyen.baseproject.repo.LoginRepository
import com.thanh_nguyen.baseproject.screens.home.HomeViewModel
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


/**
 * module for view model dependencies
 */

const val VIEW_MODEL_MODULE = "view_model_module"

val viewModelModule = Kodein.Module(VIEW_MODEL_MODULE, false) {
    bind<ViewModelProvider.Factory>() with singleton {
        ViewModelFactory(kodein.direct)
    }

    bindViewModel<HomeViewModel>() with provider {
        HomeViewModel()
    }

//    bindViewModel<LoginViewModel>() with provider {
//        LoginViewModel(instance())
//    }
//
//    bindViewModel<PlaygroundViewModel>() with provider {
//        PlaygroundViewModel(instance())
//    }
}