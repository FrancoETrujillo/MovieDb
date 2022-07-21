package com.ftrujillo.moviedbsample.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import timber.log.Timber

class NetworkInfoProvider(context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val callbackList = mutableListOf<NetworkStatusChangeListener>()

    /**
     * Checks if network with internet connection is available
     */
    @Suppress("DEPRECATION")
    fun isNetworkAvailable(): Boolean {
        var result = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }
        return result
    }

    internal fun notifyNetworkChanges(isConnected: Boolean) {
        if (callbackList.isNotEmpty()) {
            for (callback in callbackList) {
                try {
                    callback.onNetworkStatusChange(isConnected)
                } catch (e: Exception) {
                    Timber.e(
                        e,
                        "Exception while trying to call listeners probably forgot unregister"
                    )
                    unSubscribeConnectionStateChangeCallback(callback)
                }
            }
        }
    }

    /**
     * Subscribe NetworkStatusChangeListener to receive callbacks about network changes
     * The Listener must be Unregistered
     */
    fun subscribeConnectionStateChangeCallback(callback: NetworkStatusChangeListener) {
        if (callbackList.isEmpty()) {
            val networkRequest = NetworkRequest.Builder()
                .build()
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        }
        callbackList.add(callback)
    }

    /**
     * Unsubscribe NetworkStatusChangeListener
     */
    private fun unSubscribeConnectionStateChangeCallback(callback: NetworkStatusChangeListener) {
        callbackList.remove(callback)
        if (callbackList.isEmpty()) {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            notifyNetworkChanges(false)
        }

        override fun onUnavailable() {
            notifyNetworkChanges(false)
        }

        override fun onAvailable(network: Network) {
            notifyNetworkChanges(true)
        }
    }

    /**
     * Interface to be implemented in order to receive network status changes from Network Provider
     */
    interface NetworkStatusChangeListener {
        fun onNetworkStatusChange(isConnected: Boolean)
    }
}