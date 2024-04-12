package com.simform.ssfurnicraftar.data.model

import com.simform.ssfurnicraftar.data.remote.model.NetworkDownloadInfo

data class DownloadInfo(
    val url: String,
    val totalSize: Long
)

fun NetworkDownloadInfo.asExternalModel() = DownloadInfo(
    url = url,
    totalSize = size
)
