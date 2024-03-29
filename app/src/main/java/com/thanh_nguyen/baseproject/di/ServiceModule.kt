package com.thanh_nguyen.baseproject.di

import com.thanh_nguyen.baseproject.network.ApiClient
import com.thanh_nguyen.baseproject.service.LoginService
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

const val SERVICE_MODULE = "SERVICE_MODULE"
val serviceModule = Kodein.Module(SERVICE_MODULE, false){
    bind() from singleton {
        ApiClient.createService<LoginService>()
    }
}