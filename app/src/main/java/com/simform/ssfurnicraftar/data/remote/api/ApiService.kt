package com.simform.ssfurnicraftar.data.remote.api

import com.simform.ssfurnicraftar.data.remote.apiresult.ApiResult
import com.simform.ssfurnicraftar.data.remote.model.NetworkDownload
import com.simform.ssfurnicraftar.data.remote.model.NetworkModel
import com.simform.ssfurnicraftar.data.remote.model.NetworkModels
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Provides remote APIs
 */
interface ApiService {

    @GET("models?categories=furniture-home&downloadable=true&archives_flavours=false")
    suspend fun getModels(
        @Query("tags") category: String = "table",
        @Query("cursor") cursor: String? = null
    ): ApiResult<NetworkModels>

    @GET("models/{modelId}")
    suspend fun getModelInfo(@Path("modelId") id: String): ApiResult<NetworkModel>

    @GET("models/{modelId}/download")
    suspend fun getDownloadInfo(@Path("modelId") id: String): ApiResult<NetworkDownload>
}
