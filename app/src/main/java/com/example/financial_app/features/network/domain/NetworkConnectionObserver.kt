package com.example.financial_app.features.network.domain

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.financial_app.R
import com.example.financial_app.common.usecase.ShowToastUseCase

class NetworkConnectionObserver(context: Context) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val showToast = ShowToastUseCase(context)
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            showToast(context.getString(R.string.error_no_internet))
        }
    }

    init {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    fun unregister() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    companion object {
        @Volatile
        private var instance: NetworkConnectionObserver? = null

        fun init(context: Context) {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = NetworkConnectionObserver(context.applicationContext)
                    }
                }
            }
        }

        fun release() {
            instance?.unregister()
            instance = null
        }
    }
}
