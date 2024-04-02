package com.simform.ssfurnicraftar.ui.download

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simform.ssfurnicraftar.data.remote.model.DownloadStatus
import com.simform.ssfurnicraftar.data.utils.FileHelper
import com.simform.ssfurnicraftar.domain.DownloadModelUseCase
import com.simform.ssfurnicraftar.domain.GetDownloadInfoUseCase
import com.simform.ssfurnicraftar.ui.download.navigation.DownloadArgs
import com.simform.ssfurnicraftar.utils.extension.checkIfExist
import com.simform.ssfurnicraftar.utils.extension.toPercentage
import com.simform.ssfurnicraftar.utils.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.nio.file.Path
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDownloadInfoUseCase: GetDownloadInfoUseCase,
    private val downloadModelUseCase: DownloadModelUseCase,
    fileHelper: FileHelper
) : ViewModel() {

    private val args = DownloadArgs(savedStateHandle)

    val productId = args.productId
    val modelColor = args.modelColor

    private val _downloadUiState = MutableStateFlow<DownloadUiState>(DownloadUiState.Loading)
    val downloadUiState = _downloadUiState.asStateFlow()

    /**
     * Directory used to store downloaded files.
     */
    private val downloadDirectory = fileHelper.getModelDir()

    init {
        download(args.productId)
    }

    private fun download(productId: String) {
        // Get local path to store the downloaded model
        val path = downloadDirectory.resolve("$productId.glb")

        viewModelScope.launch {
            getDownloadInfoUseCase(productId).collectLatest { result ->
                when (result) {
                    Result.Loading -> {
                        _downloadUiState.emit(DownloadUiState.Loading)
                    }

                    is Result.Success -> {
                        // Check if the model is already downloaded and has the same size
                        // as available in download info
                        if (path.checkIfExist(result.data.totalSize)) {
                            _downloadUiState.emit(DownloadUiState.Completed(path))
                            return@collectLatest
                        }
                        startDownload(path, result.data.url)
                    }

                    is Result.Error -> {
                        Timber.d("Failed to get product $productId details.")
                        // Use cached model if exist
                        if (path.checkIfExist()) {
                            Timber.d("Using cached model.")
                            _downloadUiState.emit(DownloadUiState.Completed(path))
                        }
                    }
                }
            }
        }
    }

    private suspend fun startDownload(path: Path, url: String) {
        downloadModelUseCase(path, url).collectLatest { downloadStatus ->
            when (downloadStatus) {
                DownloadStatus.Loading -> {
                    _downloadUiState.emit(DownloadUiState.Loading)
                }

                is DownloadStatus.Error -> {
                    Timber.e(downloadStatus.message, downloadStatus.cause)
                    _downloadUiState.emit(
                        DownloadUiState.Failed(
                            downloadStatus.message
                        )
                    )
                }

                is DownloadStatus.Progress -> {
                    _downloadUiState.emit(
                        DownloadUiState.Progress(
                            downloadStatus.downloaded,
                            downloadStatus.total
                        ).also { Timber.i("Progress: ${it.progress.toPercentage()}") }
                    )
                }

                DownloadStatus.Completed -> {
                    _downloadUiState.emit(
                        DownloadUiState.Completed(path)
                    )
                }
            }
        }
    }
}
