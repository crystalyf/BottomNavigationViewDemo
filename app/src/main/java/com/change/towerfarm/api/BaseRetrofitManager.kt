package com.change.towerfarm.api

import com.google.common.collect.Sets
import okhttp3.Request
import retrofit2.Call

open class BaseRetrofitManager {

    val apiPool: MutableSet<Request> = Sets.newConcurrentHashSet()

    /**
     *  exclusive run of apis
     *  @param [apiCall] retrofit api result
     *  @param [apiCallback] retrofit api callback
     *  @param [T] type of response data
     */
    protected fun <T> exclusiveRun(apiCall: Call<T>, apiCallback: ApiCallBack<T>) {
        val exclusiveRequest = apiCall.request()
        apiCallback.onFinished = {
            apiPool.remove(exclusiveRequest)
        }
        if (!apiPool.contains(exclusiveRequest)) {
            apiCall.enqueue(apiCallback)
            apiPool.add(exclusiveRequest)
        }
    }
}