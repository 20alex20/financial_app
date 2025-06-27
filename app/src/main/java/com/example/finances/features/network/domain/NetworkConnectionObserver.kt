package com.example.finances.features.network.domain

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.widget.Toast
import com.example.finances.R

class NetworkConnectionObserver private constructor(context: Context) {
    private val connectivityManager = context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        private val toast: Toast = Toast.makeText(
            context,
            R.string.error_no_internet,
            Toast.LENGTH_SHORT
        )

        override fun onLost(network: Network) = toast.show()
    }

    fun unregister() = connectivityManager.unregisterNetworkCallback(networkCallback)

    init {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    companion object {
        @Volatile
        private var instance: NetworkConnectionObserver? = null

        fun init(context: Context) {
            if (instance == null) synchronized(this) {
                if (instance == null)
                    instance = NetworkConnectionObserver(context.applicationContext)
            }
        }

        fun release() {
            instance?.unregister()
            instance = null
        }
    }
}
