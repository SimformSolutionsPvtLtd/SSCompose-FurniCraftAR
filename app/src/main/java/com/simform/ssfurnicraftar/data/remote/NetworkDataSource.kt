package com.simform.ssfurnicraftar.data.remote

import com.simform.ssfurnicraftar.data.remote.api.ApiService
import com.simform.ssfurnicraftar.data.remote.apiresult.ApiResult
import com.simform.ssfurnicraftar.data.remote.model.NetworkModels
import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getModels(tag: String): ApiResult<NetworkModels> = apiService.getModels(tag)
}
