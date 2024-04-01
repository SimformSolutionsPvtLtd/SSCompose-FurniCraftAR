package com.simform.ssfurnicraftar.data.remote.api

import com.simform.ssfurnicraftar.data.remote.apiresult.ApiResult
import com.simform.ssfurnicraftar.data.remote.model.NetworkModels
import retrofit2.http.GET
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
}
