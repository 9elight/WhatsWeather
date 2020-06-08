package com.delight.whatsweather.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo


class ServiceManager(base: Context) {
     val context: Context = base

    fun isNetworkAvailable(): Boolean {
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}