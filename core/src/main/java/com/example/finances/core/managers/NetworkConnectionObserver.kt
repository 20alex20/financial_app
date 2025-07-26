package com.example.finances.core.managers

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.finances.core.di.CoreScope
import com.example.finances.core.di.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@CoreScope
class NetworkConnectionObserver @Inject constructor(@ApplicationContext context: Context) {
    private val _isOnline = MutableSharedFlow<Boolean>(replay = 1)
    private val isOnline: SharedFlow<Boolean> = _isOnline.asSharedFlow()

    private val connectivityManager = context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _isOnline.tryEmit(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            _isOnline.tryEmit(false)
        }
    }

    init {
        val request = NetworkRequest.Builder().addCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET
        ).build()
        connectivityManager.registerNetworkCallback(request, callback)

        val currentState = connectivityManager.activeNetwork?.let { network ->
            connectivityManager.getNetworkCapabilities(network)?.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            )
        } ?: false
        _isOnline.tryEmit(currentState)
    }

    fun observe(): Flow<Boolean> = isOnline
}
