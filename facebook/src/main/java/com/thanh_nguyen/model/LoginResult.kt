package com.thanh_nguyen.model

import com.facebook.GraphResponse
import com.facebook.login.LoginResult

data class FacebookLoginResult(
    val token: String?,
    val id: String?,
    val email: String?,
    val name: String?,
    val avatar: String?
)
