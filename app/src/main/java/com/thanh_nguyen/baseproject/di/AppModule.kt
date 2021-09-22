package com.thanh_nguyen.baseproject.di

import com.thanh_nguyen.baseproject.firebase.FirebaseManager
import com.thanh_nguyen.helper.WifiHelper
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

const val APP_MODULE = "app_module"

val appModule = Kodein.Module(APP_MODULE, false){
    import(serviceModule)
    import(remoteModule)
    import(repositoryModule)
    import(viewModelModule)

//    bind() from singleton {
//        FirebaseManager()
//    }
//
    bind() from provider {
        WifiHelper()
    }
}