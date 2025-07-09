package com.example.finances.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.widget.Toast
import com.example.finances.R
import com.example.finances.core.di.ActivityContext
import com.example.finances.core.di.ActivityScope
import com.example.finances.core.di.ApplicationContext
import javax.inject.Inject

/**
 * Отвечает за подписку на событие о потере соединения и отображение соответствующего сообщения
 */
@ActivityScope
class NetworkConnectionObserver @Inject constructor(
    @ApplicationContext appContext: Context,
    @ActivityContext activityContext: Context
) {
    private val connectivityManager = appContext.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        private val toast = Toast.makeText(
            activityContext,
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
}
