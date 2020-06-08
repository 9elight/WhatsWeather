package com.delight.whatsweather.utils.broadcastReciever

interface ConnectivityListener{
    fun onNetworkConnectionChanged(isConnected: Boolean)
}