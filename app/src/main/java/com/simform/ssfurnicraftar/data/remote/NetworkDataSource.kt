package com.simform.ssfurnicraftar.data.remote

import com.simform.ssfurnicraftar.data.remote.api.ApiService
import com.simform.ssfurnicraftar.data.remote.api.DownloadService
import com.simform.ssfurnicraftar.data.remote.apiresult.ApiResult
import com.simform.ssfurnicraftar.data.remote.model.DownloadStatus
import com.simform.ssfurnicraftar.data.remote.model.NetworkDownload
import com.simform.ssfurnicraftar.data.remote.model.NetworkModel
import com.simform.ssfurnicraftar.data.remote.model.NetworkModels
import kotlinx.coroutines.flow.Flow
import java.nio.file.Path
import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val apiService: ApiService,
    private val downloadService: DownloadService,
) {

    suspend fun getModels(tag: String, cursor: String): ApiResult<NetworkModels> =
        apiService.getModels(tag, cursor)

    suspend fun getModelInfo(modelId: String): ApiResult<NetworkModel> =
        apiService.getModelInfo(modelId)

    suspend fun getDownloadInfo(modelId: String): ApiResult<NetworkDownload> =
        apiService.getDownloadInfo(modelId)

    suspend fun downloadModel(path: Path, url: String): Flow<DownloadStatus> =
        downloadService.download(path, url)
}
