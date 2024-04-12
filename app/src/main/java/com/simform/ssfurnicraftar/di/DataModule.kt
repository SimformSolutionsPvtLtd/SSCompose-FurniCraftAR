package com.simform.ssfurnicraftar.di

import com.simform.ssfurnicraftar.data.repository.ModelRepository
import com.simform.ssfurnicraftar.data.repository.ModelRepositoryImpl
import com.simform.ssfurnicraftar.data.utils.ConnectivityManagerNetworkMonitor
import com.simform.ssfurnicraftar.data.utils.FileHelper
import com.simform.ssfurnicraftar.data.utils.FileHelperImpl
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

    @Binds
    fun bindsFileHelper(fileHelper: FileHelperImpl): FileHelper

    @Binds
    fun bindsModelRepository(modelRepository: ModelRepositoryImpl): ModelRepository
}
