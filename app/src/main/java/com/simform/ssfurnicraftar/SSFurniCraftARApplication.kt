package com.simform.ssfurnicraftar

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.simform.ssfurnicraftar.utils.NetworkUtil
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class SSFurniCraftARApplication : Application() {

    private val validNetworks: MutableList<Network> = mutableListOf()

    override fun onCreate() {
        super.onCreate()

        initTimber()
        observeNetwork()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            // Show logs only when on debug
            Timber.plant(Timber.DebugTree())
        }
    }

    /**
     * Observe network state.
     */
    private fun observeNetwork() {
        val connectivityManager: ConnectivityManager =
            getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (!hasInternet(capabilities)) {
            checkValidNetworks()
        }
        connectivityManager.registerNetworkCallback(networkRequest, object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                if (hasInternet(networkCapabilities)) {
                    validNetworks.add(network)
                    checkValidNetworks()
                }
            }

            override fun onLost(network: Network) {
                validNetworks.remove(network)
                checkValidNetworks()
            }
        })
    }

    private fun checkValidNetworks() {
        NetworkUtil.isNetworkConnected = validNetworks.isNotEmpty()
    }

    private fun hasInternet(networkCapabilities: NetworkCapabilities?): Boolean =
        networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}
