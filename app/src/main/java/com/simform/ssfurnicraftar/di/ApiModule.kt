package com.simform.ssfurnicraftar.di

import android.content.Context
import com.simform.ssfurnicraftar.BuildConfig
import com.simform.ssfurnicraftar.data.remote.api.ApiService
import com.simform.ssfurnicraftar.data.remote.apiresult.ApiResultCallAdapterFactory
import com.simform.ssfurnicraftar.data.remote.interceptor.ApiAuthenticator
import com.simform.ssfurnicraftar.utils.constant.Urls
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

private const val HTTP_LOGGING_INTERCEPTOR = "HTTP_LOGGING_INTERCEPTOR"
private const val HTTP_DOWNLOAD_LOGGING_INTERCEPTOR = "HTTP_DOWNLOAD_LOGGING_INTERCEPTOR"
private const val OKHTTP_CLIENT = "OKHTTP_CLIENT"
const val OKHTTP_DOWNLOAD_CLIENT = "OKHTTP_DOWNLOAD_CLIENT"
private const val CACHE_SIZE: Long = 10 * 1024 * 1024 // 10 MB
private const val NETWORK_CALL_TIMEOUT: Long = 1 // Minute

/**
 * Provides remote APIs dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    @Named(HTTP_LOGGING_INTERCEPTOR)
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
    }

    @Singleton
    @Provides
    @Named(HTTP_DOWNLOAD_LOGGING_INTERCEPTOR)
    fun providesHttpDownloadLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
            // Get only Headers for debugging. Logging Body will download whole response (file body)
            // at once and progress won't work.
                HttpLoggingInterceptor.Level.HEADERS
            else
                HttpLoggingInterceptor.Level.NONE
        }

    @Singleton
    @Provides
    fun providesApiAuthenticator(): Authenticator = ApiAuthenticator()

    @Singleton
    @Provides
    @Named(OKHTTP_CLIENT)
    fun providesOkHttpClient(
        @ApplicationContext context: Context,
        @Named(HTTP_LOGGING_INTERCEPTOR) httpLoggingInterceptor: HttpLoggingInterceptor,
        authenticator: Authenticator
    ): OkHttpClient = OkHttpClient.Builder()
        .cache(Cache(context.cacheDir, CACHE_SIZE))
        .addInterceptor(httpLoggingInterceptor)
        .authenticator(authenticator)
        .readTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.MINUTES)
        .writeTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.MINUTES)
        .build()

    @Singleton
    @Provides
    @Named(OKHTTP_DOWNLOAD_CLIENT)
    fun providesOkHttpDownloadClient(
        @Named(HTTP_DOWNLOAD_LOGGING_INTERCEPTOR) httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .readTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.MINUTES)
        .writeTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.MINUTES)
        .build()

    @Singleton
    @Provides
    fun providesRetrofit(
        @Named(OKHTTP_CLIENT) okHttpClient: OkHttpClient,
        apiResultCallAdapterFactory: ApiResultCallAdapterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(
            Urls.getBaseUrl()
                .also { Timber.d("Setting Base URL : $it") }
        )
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(apiResultCallAdapterFactory)
        .build()

    @Singleton
    @Provides
    fun providesApiService(
        retrofit: Retrofit
    ): ApiService = retrofit.create(ApiService::class.java)
}
