package com.thanh_nguyen.baseproject.network.remote

import com.thanh_nguyen.baseproject.network.ApiClient
import com.thanh_nguyen.baseproject.service.LoginService

class LoginRemoteDataSource(private val loginService: LoginService): BaseRemoteDataSource() {
    fun getAuthorInfo() = getResult {
        loginService.getAuthorInfo()
    }
}