package com.simform.ssfurnicraftar.ui.download

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simform.ssfurnicraftar.R
import com.simform.ssfurnicraftar.ui.arview.ColorState
import com.simform.ssfurnicraftar.ui.component.QuitDialog
import com.simform.ssfurnicraftar.ui.theme.LocalDimens
import com.simform.ssfurnicraftar.utils.constant.Constants
import com.simform.ssfurnicraftar.utils.extension.toPercentage
import timber.log.Timber
import java.nio.file.Path
import kotlin.math.roundToInt

@Composable
fun DownloadRoute(
    modifier: Modifier = Modifier,
    onDownloadComplete: (String, Path, ColorState) -> Unit,
    viewModel: DownloadViewModel = hiltViewModel()
) {
    val downloadUiState by viewModel.downloadUiState.collectAsStateWithLifecycle()

    DownloadScreen(
        modifier = modifier,
        downloadUiState = downloadUiState,
        onDownloadComplete = { path ->
            onDownloadComplete(viewModel.productId, path, viewModel.modelColor)
        }
    )
}

@Composable
private fun DownloadScreen(
    modifier: Modifier = Modifier,
    downloadUiState: DownloadUiState,
    onDownloadComplete: (Path) -> Unit
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    var showQuitDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = !showQuitDialog) {
        showQuitDialog = true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(LocalDimens.SpacingLarge),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(LocalDimens.IconXL),
                imageVector = Icons.Default.CloudDownload,
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(LocalDimens.SpacingMedium))

            when (downloadUiState) {
                DownloadUiState.Loading -> InitialLoading()

                is DownloadUiState.Progress -> DownloadProgress(
                    progressState = downloadUiState
                )

                is DownloadUiState.Completed -> {
                    DownloadComplete(location = downloadUiState.path.toString())
                    onDownloadComplete(downloadUiState.path)
                }

                is DownloadUiState.Failed -> Timber.d(downloadUiState.reason, downloadUiState.e)
            }
        }

        if (showQuitDialog) {
            QuitDialog(
                message = stringResource(id = R.string.title_quit_download),
                onDismiss = { showQuitDialog = false }
            ) {
                showQuitDialog = false
                onBackPressedDispatcher?.onBackPressed()
            }
        }
    }
}

@Composable
private fun InitialLoading(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(modifier = modifier)
}

@Composable
private fun DownloadProgress(
    modifier: Modifier = Modifier,
    progressState: DownloadUiState.Progress
) {

    val progress by animateFloatAsState(
        targetValue = progressState.progress,
        label = Constants.LABEL_DOWNLOAD_PROGRESS,
        animationSpec = tween(durationMillis = Constants.DOWNLOAD_PROGRESS_INTERVAL)
    )

    Column(
        modifier = modifier.fillMaxWidth(LocalDimens.Download.ProgressWidthPercentage),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = LocalDimens.SpacingSmall),
            progress = { progress },
            strokeCap = StrokeCap.Round
        )

        Text(
            text = stringResource(
                id = R.string.downloading,
                progressState.progress.toPercentage().roundToInt()
            )
        )
    }
}

@Composable
private fun DownloadComplete(
    modifier: Modifier = Modifier,
    location: String
) {
    Text(
        modifier = modifier,
        text = stringResource(R.string.download_completed, location)
    )
}
