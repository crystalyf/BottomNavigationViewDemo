package com.change.towerfarm.api.exceptions

import com.google.gson.annotations.SerializedName

// server business error
data class ApiError(
    @SerializedName("ret")
    val code: String,
    @SerializedName("err")
    val errorCode: String,
    //fixme： 描述文言
   // val detail: String
)