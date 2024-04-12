package com.simform.ssfurnicraftar.ui.download

import java.nio.file.Path

sealed interface DownloadUiState {

    data object Loading : DownloadUiState

    data class Progress(
        val downloaded: Long,
        val total: Long
    ) : DownloadUiState

    data class Completed(val path: Path) : DownloadUiState

    data class Failed(val reason: String, val e: Exception? = null) : DownloadUiState
}

/**
 * Get progress from downloaded and total download value.
 */
val DownloadUiState.Progress.progress: Float
    get() = downloaded.toFloat() / total.toFloat()
