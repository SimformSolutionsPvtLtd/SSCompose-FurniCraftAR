package com.simform.ssfurnicraftar.di

import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.InstallIn

/**
 * Defines all the classes that need to be provided in the scope of the app.
 * If they are singleton mark them as '@Singleton'.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule
