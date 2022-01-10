package com.change.towerfarm.utils

import android.content.Context

class SharedPrefsManager(private val context: Context) {
    private val preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

    companion object {
        private const val PREFERENCES = "sPrefs"
        const val hasLogin = "hasLogin"
        const val googleAuthToken = "googleAuthToken"

        @Synchronized
        fun newInstance(context: Context) = SharedPrefsManager(context)
    }

    fun putBoolean(key: String, value: Boolean) = preferences.edit().putBoolean(key, value).apply()

    fun putInt(key: String, value: Int) = preferences.edit().putInt(key, value).apply()

    fun getBoolean(key: String, defValue: Boolean) = preferences.getBoolean(key, defValue)

    fun getInt(key: String, defValue: Int) = preferences.getInt(key, defValue)


    //google auth token
    fun getGoogleAuthToken(): String? = preferences.getString(googleAuthToken, null)
    fun putGoogleAuthToken(value: String) =
        preferences.edit().putString(googleAuthToken, value).apply()

    //初回ログイン
    fun getHasLogin(): Boolean {
        return preferences.getBoolean(hasLogin, false)
    }

    fun putHasLogin(value: Boolean) {
        preferences.edit().putBoolean(hasLogin, value).apply()
    }




}
