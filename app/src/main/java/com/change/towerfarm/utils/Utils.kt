package com.change.towerfarm.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.inputmethod.InputMethodManager
import com.change.towerfarm.base.GatewayApplication
import timber.log.Timber


object Utils {

    /**
     * メソッドがコールのログ出力
     */
    inline fun <T> timberTrace(instance: Class<T>) {
        Timber.v(
            "%s: %s",
            instance.simpleName,
            Thread.currentThread().stackTrace[2].methodName
        )
    }

    /**
     * ソフトキーボードを非表示にする
     */
    fun hideSoftKeyboard(activity: Activity) {
        if (activity.currentFocus != null && activity.currentFocus!!.windowToken != null) {
            val manager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(
                activity.currentFocus!!
                    .windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    /**
     * ネットワークは利用可能ですか？
     */
    fun isNetworkAvailable(application: GatewayApplication): Boolean {
        val connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }
}