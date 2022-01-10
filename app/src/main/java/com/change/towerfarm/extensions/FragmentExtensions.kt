package com.change.towerfarm.extensions

import androidx.fragment.app.Fragment
import com.change.towerfarm.base.GatewayApplication
import com.change.towerfarm.utils.SharedPrefsManager
import com.change.towerfarm.viewmodels.ViewModelFactory

fun Fragment.getViewModelFactory(): ViewModelFactory {
    val sharePref = SharedPrefsManager(requireContext().applicationContext)
    val apiRepository = (requireContext().applicationContext as GatewayApplication).apiRepository
    return ViewModelFactory.getInstance(sharePref, apiRepository)
}