package com.change.towerfarm.base

import com.google.gson.annotations.SerializedName

data class BaseResultModel(
    @SerializedName("ret")
    val code: String,
    @SerializedName("err")
    val errorCode: String
    //val detail: T
)