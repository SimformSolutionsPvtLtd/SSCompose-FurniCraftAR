package com.simform.ssfurnicraftar.di

import android.content.Context
import androidx.room.Room
import com.simform.ssfurnicraftar.data.local.database.SSFurniCraftARDatabase
import com.simform.ssfurnicraftar.data.local.database.dao.CategoryAndModelDao
import com.simform.ssfurnicraftar.data.local.database.dao.ModelDao
import com.simform.ssfurnicraftar.data.local.database.dao.RemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesSSFurniCraftARDatabase(
        @ApplicationContext context: Context,
    ): SSFurniCraftARDatabase = Room.databaseBuilder(
        context,
        SSFurniCraftARDatabase::class.java,
        "ssfurnicraftar-database",
    ).build()

    @Provides
    fun providesModelDao(
        database: SSFurniCraftARDatabase
    ): ModelDao = database.modelDao()

    @Provides
    fun providesRemoteKeyDao(
        database: SSFurniCraftARDatabase
    ): RemoteKeyDao = database.remoteKeyDao()

    @Provides
    fun providesCategoryAndModelDao(
        database: SSFurniCraftARDatabase
    ): CategoryAndModelDao = database.categoryAndModelDao()
}
