package com.thanh_nguyen.baseproject.model.respone

import com.thanh_nguyen.baseproject.model.BaseModel


data class BaseResponse<T>(
    val success: String?,
    val result_code: Int?,
    val result: String?,
    val data: T?
): BaseModel()