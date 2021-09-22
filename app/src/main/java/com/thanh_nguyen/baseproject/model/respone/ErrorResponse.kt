package com.thanh_nguyen.baseproject.model.respone

import com.thanh_nguyen.baseproject.model.BaseModel

data class ErrorResponse (
    var result: String?,
    var result_code: Int?
): BaseModel()
