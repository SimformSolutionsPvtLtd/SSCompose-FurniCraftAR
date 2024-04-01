package com.simform.ssfurnicraftar.di

import com.simform.ssfurnicraftar.data.utils.ConnectivityManagerNetworkMonitor
import com.simform.ssfurnicraftar.data.utils.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindNetworkMonitor(networkMonitor: ConnectivityManagerNetworkMonitor): NetworkMonitor
}
