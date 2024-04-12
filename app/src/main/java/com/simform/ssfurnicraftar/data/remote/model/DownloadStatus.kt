package com.simform.ssfurnicraftar.data.remote.model

sealed interface DownloadStatus {

    data object Loading : DownloadStatus

    data object Completed : DownloadStatus

    data class Progress(
        val downloaded: Long,
        val total: Long
    ) : DownloadStatus

    data class Error(val message: String, val cause: Throwable? = null) : DownloadStatus
}
