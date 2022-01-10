package com.change.towerfarm.base

import android.app.Application
import com.change.towerfarm.repository.ApiRepository
import com.change.towerfarm.repository.IApiRepository
import com.change.towerfarm.utils.SharedPrefsManager
import com.facebook.FacebookSdk

class GatewayApplication : Application() {

    companion object {
        var instance: GatewayApplication? = null
    }

    private val prefs by lazy { SharedPrefsManager.newInstance(this) }

    val apiRepository: IApiRepository
        get() = ApiRepository.instance(this)

    override fun onCreate() {
        super.onCreate()
        FacebookSdk.sdkInitialize(applicationContext)
        instance = this
    }
}