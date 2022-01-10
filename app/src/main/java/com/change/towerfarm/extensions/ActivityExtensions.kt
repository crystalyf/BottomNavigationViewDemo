package com.change.towerfarm.extensions

import android.app.Activity
import com.change.towerfarm.base.GatewayApplication
import com.change.towerfarm.utils.SharedPrefsManager
import com.change.towerfarm.viewmodels.ViewModelFactory

fun Activity.getViewModelFactory(): ViewModelFactory {
    val sharePref = SharedPrefsManager(applicationContext)
    val apiRepository = (applicationContext as GatewayApplication).apiRepository
    return ViewModelFactory.getInstance(sharePref, apiRepository)
}