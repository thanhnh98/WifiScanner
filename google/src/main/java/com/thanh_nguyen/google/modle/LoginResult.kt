package com.thanh_nguyen.google.modle

data class LoginResult(
    val idToken: String?,
    val email: String?,
    val displayName: String?,
    val avatar: String?,
    val personId: String?
)
