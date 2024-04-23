package com.simform.ssfurnicraftar.ui.arview

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simform.ssfurnicraftar.R
import com.simform.ssfurnicraftar.ui.arview.component.ARView
import com.simform.ssfurnicraftar.ui.arview.component.Options
import com.simform.ssfurnicraftar.ui.component.QuitDialog
import com.simform.ssfurnicraftar.utils.constant.Urls
import com.simform.ssfurnicraftar.utils.extension.shareImage
import kotlinx.coroutines.launch

@Composable
fun ARViewRoute(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    viewModel: ARViewViewModel = hiltViewModel()
) {
    val arViewUiState by viewModel.arViewUiState.collectAsStateWithLifecycle()

    ARViewScreen(
        modifier = modifier,
        arViewUiState = arViewUiState,
        onColorChange = viewModel::changeColor,
        onShare = viewModel::createShareUri,
        onNavigateBack = onNavigateBack,
        onShowSnackbar = onShowSnackbar
    )
}

@Composable
private fun ARViewScreen(
    modifier: Modifier = Modifier,
    arViewUiState: ARViewUiState,
    onColorChange: (ColorState) -> Unit,
    onShare: (Bitmap) -> Unit,
    onNavigateBack: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    var showQuitDialog by remember { mutableStateOf(false) }
    var captureImage by rememberSaveable {
        mutableStateOf(false)
    }
    var capturedImage by remember {
        mutableStateOf<Bitmap?>(null)
    }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val shareMessage = stringResource(
        R.string.message_share,
        Urls.getModelUrl(arViewUiState.productId, arViewUiState.modelColor)
    )
    val modelPlacedMessage = stringResource(R.string.model_placed_successfully)

    val dynamicColorMessage = stringResource(R.string.dynamic_color_enabled)

    var rotationEnabled by rememberSaveable { mutableStateOf(false) }
    var showRotationChangeMessage by rememberSaveable { mutableStateOf(false) }
    val rotationStartedMessage = stringResource(R.string.message_auto_rotation_started)
    val rotationStoppedMessage = stringResource(R.string.message_auto_rotation_stopped)

    LaunchedEffect(key1 = capturedImage) {
        capturedImage?.let { bitmap ->
            captureImage = false
            onShare(bitmap)
        }
    }

    LaunchedEffect(key1 = arViewUiState.shareUri) {
        arViewUiState.shareUri?.let { uri ->
            context.shareImage(uri, shareMessage)
        }
    }

    LaunchedEffect(key1 = arViewUiState.modelColor) {
        if (arViewUiState.modelColor is ColorState.Dynamic) {
            onShowSnackbar(dynamicColorMessage, null)
        }
    }

    LaunchedEffect(key1 = showRotationChangeMessage, key2 = rotationEnabled) {
        if (showRotationChangeMessage) {
            onShowSnackbar(
                if (rotationEnabled) rotationStartedMessage else rotationStoppedMessage,
                null
            )
            showRotationChangeMessage = false
        }
    }

    BackHandler {
        showQuitDialog = !showQuitDialog
    }

    Box(modifier = modifier) {
        ARView(
            arViewUiState = arViewUiState,
            rotationEnabled = rotationEnabled,
            onStopRotation = {
                if (rotationEnabled) {
                    rotationEnabled = false
                    showRotationChangeMessage = true
                }
            },
            enableCapture = captureImage,
            onCapture = { capturedImage = it },
        ) {
            coroutineScope.launch {
                onShowSnackbar(modelPlacedMessage, null)
            }
        }

        Options(
            rotationEnabled = rotationEnabled,
            onRotationToggle = {
                rotationEnabled = !rotationEnabled
                showRotationChangeMessage = true
            },
            onShare = { captureImage = true },
            arViewUiState = arViewUiState,
            onColorChange = onColorChange
        )

        if (showQuitDialog) {
            QuitDialog(
                message = stringResource(R.string.title_arview_quit),
                onDismiss = { showQuitDialog = false }
            ) {
                showQuitDialog = false
                onNavigateBack()
            }
        }
    }
}
