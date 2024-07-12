package com.example.test.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object NetworkUtils {
    fun isConnectedToWifi(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
    }

    fun isConnectedToCellular(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
    }

    fun isOffline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetwork == null
    }

    // Function to check connectivity (extracted for reusability)
    fun checkConnectivity(context: Context): Boolean {
        return when {
            NetworkUtils.isConnectedToWifi(context) -> {
                // Device is connected to Wi-Fi
                true
            }
            NetworkUtils.isConnectedToCellular(context) -> {
                // Device is connected to cellular data (4G/LTE, etc.)
                true
            }
            NetworkUtils.isOffline(context) -> {
                // Device is offline
                false
            }
            else -> false
        }
    }
}