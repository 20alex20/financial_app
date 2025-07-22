package com.example.finances.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.widget.Toast
import com.example.finances.core.R
import com.example.finances.core.di.CoreScope
import com.example.finances.core.di.ApplicationContext
import javax.inject.Inject

/**
 * Responsible for subscribing to connection loss events and displaying corresponding messages
 */
@CoreScope
class NetworkConnectionObserver @Inject constructor(@ApplicationContext context: Context) {
    private val connectivityManager = context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        private val toast = Toast.makeText(context, R.string.error_no_internet, Toast.LENGTH_SHORT)

        override fun onLost(network: Network) = toast.show()
    }

    fun unregister() = connectivityManager.unregisterNetworkCallback(networkCallback)

    init {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }
}
