package com.change.towerfarm.api

import android.text.TextUtils
import com.change.towerfarm.api.exceptions.ApiError
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import com.change.towerfarm.api.model.Result
import com.change.towerfarm.utils.Constants
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class ApiCallBack<T> constructor(
    private val con: Continuation<Result<T>>,
) : Callback<T> {
    private var continuation: Continuation<Result<T>>? = con
    var onFinished = {}

    override fun onResponse(call: Call<T>, response: Response<T>) {
        onFinished.invoke()
        if (response.isSuccessful) {
            //TODO：error code not 000
            if (response.body() == null) {
                continuation?.resume(Result.Success("" as T))
            } else {
                continuation?.resume(Result.Success(response.body()!!))
            }

        } else {
            var apiError: ApiError = try {
                Gson().fromJson(response.errorBody()?.string(), ApiError::class.java)
            } catch (e: Exception) {
                ApiError(Constants.UNKNOWN_ERROR, "")
            }
            if (TextUtils.isEmpty(apiError.code)) {
                apiError = ApiError(Constants.UNKNOWN_ERROR, "")
            }
            continuation?.resume(Result.Error(null, apiError))
        }
        continuation = null
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        Timber.e(t)
        continuation?.resume(Result.Error(t as Exception))
        continuation = null
    }

}