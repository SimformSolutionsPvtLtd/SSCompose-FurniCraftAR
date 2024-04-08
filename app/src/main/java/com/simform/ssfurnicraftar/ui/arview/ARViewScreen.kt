package com.simform.ssfurnicraftar.ui.arview

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.filament.Engine
import com.google.android.filament.MaterialInstance
import com.google.ar.core.CameraConfig
import com.google.ar.core.CameraConfigFilter
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.Pose
import com.google.ar.core.TrackingFailureReason
import com.simform.ssfurnicraftar.R
import com.simform.ssfurnicraftar.ui.component.ColorPicker
import com.simform.ssfurnicraftar.ui.component.QuitDialog
import com.simform.ssfurnicraftar.ui.theme.LocalDimens
import com.simform.ssfurnicraftar.utils.constant.Constants
import com.simform.ssfurnicraftar.utils.constant.Urls
import com.simform.ssfurnicraftar.utils.extension.captureImage
import com.simform.ssfurnicraftar.utils.extension.clone
import com.simform.ssfurnicraftar.utils.extension.enableGestures
import com.simform.ssfurnicraftar.utils.extension.setColor
import com.simform.ssfurnicraftar.utils.extension.shareImage
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.arcore.position
import io.github.sceneview.ar.getDescription
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Scale
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberView
import kotlinx.coroutines.launch
import java.util.EnumSet
import io.github.sceneview.math.Color as SVColor

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
    onColorChange: (Color?) -> Unit,
    onShare: (Bitmap) -> Unit,
    onNavigateBack: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    // Handle back press and show confirmation dialog when quiting AR View
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

    var rotationEnabled by rememberSaveable { mutableStateOf(false) }

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

    BackHandler {
        showQuitDialog = !showQuitDialog
    }

    Box(modifier = modifier) {
        ARView(
            arViewUiState = arViewUiState,
            rotationEnabled = rotationEnabled,
            onStopRotation = { rotationEnabled = false },
            enableCapture = captureImage,
            onCapture = { capturedImage = it },
        ) {
            coroutineScope.launch {
                onShowSnackbar(modelPlacedMessage, null)
            }
        }

        Options(
            rotationEnabled = rotationEnabled,
            onRotationToggle = { rotationEnabled = !rotationEnabled },
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

@Composable
private fun ARView(
    modifier: Modifier = Modifier,
    arViewUiState: ARViewUiState,
    rotationEnabled: Boolean,
    onStopRotation: () -> Unit,
    enableCapture: Boolean = false,
    onCapture: ((Bitmap) -> Unit)? = null,
    onModelPlace: () -> Unit
) {
    // The destroy calls are automatically made when their disposable effect leaves
    // the composition or its key changes.
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val childNodes = rememberNodes()
    val view = rememberView(engine)

    // Currently loaded model
    var model by remember { mutableStateOf<ModelNode?>(null) }

    // Whether to show detected plane or not
    var planeRenderer by remember { mutableStateOf(true) }

    // Reason for plane detection failure
    var trackingFailureReason by remember {
        mutableStateOf<TrackingFailureReason?>(null)
    }
    var frame by remember { mutableStateOf<Frame?>(null) }

    // Store current view that can be used to capture image
    var arSceneView by remember { mutableStateOf<ARSceneView?>(null) }

    var animateModel by remember { mutableStateOf(false) }
    var originalMaterials by remember { mutableStateOf<List<List<MaterialInstance>>?>(null) }
    var colorMaterials by remember { mutableStateOf<List<List<MaterialInstance>>?>(null) }
    var showCoachingOverlay by remember { mutableStateOf(true) }
    var showGestureOverlay by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = enableCapture) {
        if (enableCapture) {
            arSceneView?.run { onCapture?.invoke(captureImage()) }
        }
    }

    LaunchedEffect(key1 = arViewUiState.modelPath) {
        val path = arViewUiState.modelPath

        model = modelLoader.loadModelInstance(path)?.let { instance ->
            ModelNode(
                modelInstance = instance
            ).apply {
                position = Position(y = Constants.MODEL_BOUNCING_HEIGHT)
                // Change models initial scale. Currently using width.
                scale = Scale(Constants.MODEL_INITIAL_SIZE / size.x)
                // Reset initial rotation
                rotation = Rotation()
                // Enable custom gestures on model
                enableGestures()
                originalMaterials = materialInstances
                colorMaterials = materialInstances.clone()
            }
        }
    }

    LaunchedEffect(key1 = animateModel) {
        if (animateModel) {
            model?.apply {
                animate(
                    initialValue = Constants.MODEL_BOUNCING_HEIGHT,
                    targetValue = Constants.MODEL_NO_HEIGHT,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = Constants.MODEL_BOUNCING_DURATION),
                        repeatMode = RepeatMode.Reverse
                    )
                ) { value, _ ->
                    position = Position(y = value)
                }
            }
        } else {
            model?.apply {
                animate(
                    initialValue = position.y,
                    targetValue = Constants.MODEL_NO_HEIGHT,
                    animationSpec = tween()
                ) { value, _ ->
                    position = Position(y = value)
                }
            }
        }
    }

    LaunchedEffect(key1 = rotationEnabled) {
        model?.apply {
            if (rotationEnabled) {
                animate(
                    initialValue = rotation.y,
                    targetValue = rotation.y + 360,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 10000)
                    )
                ) { value, _ ->
                    rotation = Rotation(y = value)
                }
            } else {
                animate(
                    initialValue = rotation.y,
                    targetValue = 0F,
                    animationSpec = tween()
                ) { value, _ ->
                    position = Rotation(y = value)
                }
            }
        }
    }

    LaunchedEffect(key1 = arViewUiState.modelColor, key2 = model) {
        if (arViewUiState.modelColor == null) {
            originalMaterials?.let { model?.materialInstances = it }
            return@LaunchedEffect
        }

        arViewUiState.modelColor.let { (r, g, b, a) ->
            colorMaterials?.let { model?.materialInstances = it }
            model?.setColor(SVColor(r, g, b, a))
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        ARScene(
            modifier = modifier.fillMaxSize(),
            childNodes = childNodes,
            engine = engine,
            view = view,
            modelLoader = modelLoader,
            onGestureListener = rememberOnGestureListener(
                onSingleTapConfirmed = { event, node ->
                    if (animateModel) {
                        onModelPlace()
                        animateModel = false
                        showGestureOverlay = true
                    }
                    onStopRotation()
                },
                onMove = { _, event, node ->
                    if (node == null) return@rememberOnGestureListener
                    // Move gesture to move model node. Instead of changing model position
                    // change anchor node position (parent of model node)
                    frame?.hitTest(event)?.firstOrNull()?.hitPose?.position?.let { position ->
                        val anchor = (node.parent as? AnchorNode) ?: node
                        anchor.worldPosition = position
                    }
                }
            ),
            sessionCameraConfig = { session ->
                // Disable Depth API
                // Depth API may give better results. But, some devices (tested on Samsung S20+)
                // creates too much lag. Reason is unknown. Looks like the image acquired
                // with `frame.acquireCameraImage()` is not realised. so we won't be able to acquire
                // new frames when the rate limit is exceeded.
                val filter = CameraConfigFilter(session)
                filter.setDepthSensorUsage(EnumSet.of(CameraConfig.DepthSensorUsage.DO_NOT_USE))
                val cameraConfigList = session.getSupportedCameraConfigs(filter)
                cameraConfigList.first()
            },
            sessionConfiguration = { session, config ->
                // Configurations for ARSession
                config.depthMode = Config.DepthMode.DISABLED
                config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
                config.instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
                config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL
                config.lightEstimationMode =
                    Config.LightEstimationMode.ENVIRONMENTAL_HDR
            },
            planeRenderer = planeRenderer,
            onTrackingFailureChanged = {
                showCoachingOverlay = it != null
                trackingFailureReason = it
            },
            onSessionUpdated = { session, updatedFrame ->
                frame = updatedFrame

                // If no model is placed then create anchor node and add
                // model node to that anchor
                if (childNodes.isEmpty()) {
                    updatedFrame.createCenterAnchorNode(engine)?.let { anchorNode ->
                        model?.let {
                            animateModel = true
                            anchorNode.addChildNode(it)
                            childNodes.add(anchorNode)
                            planeRenderer = false
                            showCoachingOverlay = false
                        }
                    }
                } else if (animateModel) {
                    (childNodes.firstOrNull() as? AnchorNode)?.apply {
                        val (x, y) = view.viewport.run {
                            with(LocalDimens.ARView) {
                                width / MODEL_PLACEMENT_WIDTH_PROPORTION to height / MODEL_PLACEMENT_HEIGHT_PROPORTION
                            }
                        }

                        val newPosition = updatedFrame.getPose(x, y)?.position ?: return@ARScene
                        worldPosition = Position(newPosition.x, newPosition.y, newPosition.z)
                    }
                }
            },
            onViewCreated = { arSceneView = this },
            onViewUpdated = { arSceneView = this }
        )

        AnimatedVisibility(visible = showCoachingOverlay) {
            ARCoachingOverlay(
                failureReason = trackingFailureReason ?: TrackingFailureReason.NONE
            )
        }

        AnimatedVisibility(showGestureOverlay) {
            ARGestureOverlay {
                showGestureOverlay = false
            }
        }
    }
}

@Composable
private fun ARCoachingOverlay(
    modifier: Modifier = Modifier,
    failureReason: TrackingFailureReason
) {
    val failureMessage = failureReason.getDescription(LocalContext.current)
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.ar_coaching_overlay))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(LocalDimens.SpacingLarge)
    ) {
        Text(
            text = failureMessage,
            style = MaterialTheme.typography.titleLarge
        )

        LottieAnimation(
            composition = composition,
            progress = { progress }
        )
    }
}

@Composable
private fun ARGestureOverlay(
    modifier: Modifier = Modifier,
    iterations: Int = 1,
    onComplete: () -> Unit
) {
    // Create lottie composition specs for all gestures
    val specs = listOf(
        R.raw.anim_move_gesture,
        R.raw.anim_rotate_gesture,
        R.raw.anim_zoom_gesture
    ).map(LottieCompositionSpec::RawRes)

    // Display messages for gesture
    val messages = listOf(
        R.string.message_move_gesture,
        R.string.message_rotate_gesture,
        R.string.message_zoom_gesture
    ).map { stringResource(it) }

    // Number of times the gestures are played
    var playedCount by rememberSaveable { mutableIntStateOf(0) }

    // Current index of gesture
    val currentGestureIndex by remember(playedCount) {
        // If each gesture is already played for [iterations] times, call onComplete
        if (playedCount >= iterations * specs.size) {
            onComplete()
        }
        mutableIntStateOf(playedCount % specs.size)
    }

    Box(
        modifier = modifier
            .wrapContentHeight()
            .padding(LocalDimens.SpacingLarge)
    ) {
        key(currentGestureIndex) {
            Text(
                text = messages[currentGestureIndex],
                style = MaterialTheme.typography.titleLarge
            )

            LottieView(compositionSpec = specs[currentGestureIndex]) {
                playedCount++
            }
        }
    }
}

@Composable
private fun LottieView(
    modifier: Modifier = Modifier,
    compositionSpec: LottieCompositionSpec,
    onEnd: () -> Unit
) {
    val composition by rememberLottieComposition(compositionSpec)
    val progress by animateLottieCompositionAsState(composition)
    LottieAnimation(
        modifier = modifier.aspectRatio(LocalDimens.ARView.GestureAnimRatio),
        composition = composition,
        contentScale = ContentScale.FillWidth,
        progress = {
            if (progress == Constants.ANIM_COMPLETE_VALUE) {
                onEnd()
            }
            progress
        }
    )
}

@Composable
private fun Options(
    modifier: Modifier = Modifier,
    arViewUiState: ARViewUiState,
    rotationEnabled: Boolean,
    onRotationToggle: () -> Unit,
    onShare: () -> Unit,
    onColorChange: (Color?) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(LocalDimens.SpacingMedium)
    ) {
        RotationOption(
            rotationEnabled = rotationEnabled,
            onClick = onRotationToggle
        )

        ShareOption(
            modifier = Modifier.align(Alignment.TopEnd),
            onShare = onShare
        )

        ColorOption(
            modifier = Modifier.align(Alignment.BottomCenter),
            selectedColor = arViewUiState.modelColor,
            onSelect = onColorChange
        )
    }
}

@Composable
fun RotationOption(
    modifier: Modifier = Modifier,
    rotationEnabled: Boolean,
    onClick: () -> Unit,
) {
    val contentColor by animateColorAsState(
        targetValue = if (rotationEnabled) Color.Blue else Color.DarkGray,
        label = "RotationIconColor"
    )

    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_rotate_360),
            contentDescription = stringResource(R.string.cd_rotate_360),
            tint = contentColor
        )
    }
}

@Composable
private fun ShareOption(
    modifier: Modifier = Modifier,
    onShare: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onShare
    ) {
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = stringResource(R.string.share)
        )
    }
}

@Composable
private fun ColorOption(
    modifier: Modifier = Modifier,
    selectedColor: Color?,
    onSelect: (Color?) -> Unit
) {
    var showPicker by rememberSaveable { mutableStateOf(false) }

    BoxWithConstraints(modifier = modifier) {
        Row(
            modifier = Modifier
                .height(LocalDimens.ARView.OptionsIconSize)
                .clip(CircleShape)
        ) {
            AnimatedVisibility(showPicker) {
                Row(
                    modifier = Modifier
                        .widthIn(
                            max = this@BoxWithConstraints.maxWidth - LocalDimens.ARView.OptionsIconSize
                        )
                        .padding(end = LocalDimens.SpacingSmall),
                    horizontalArrangement = Arrangement.spacedBy(LocalDimens.SpacingSmall)
                ) {
                    Button(
                        modifier = Modifier.size(LocalDimens.ARView.OptionsIconSize),
                        onClick = {
                            onSelect(null)
                        },
                        contentPadding = PaddingValues(LocalDimens.NoSpacing)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = stringResource(R.string.reset_color)
                        )
                    }

                    ColorPicker(
                        modifier = Modifier,
                        initialColor = selectedColor,
                        onSelect = onSelect
                    )
                }
            }

            Button(
                modifier = Modifier.size(LocalDimens.ARView.OptionsIconSize),
                onClick = { showPicker = !showPicker },
                contentPadding = PaddingValues(LocalDimens.NoSpacing)
            ) {
                val iconDescription = stringResource(R.string.color_picker)
                AnimatedContent(targetState = showPicker, label = "ColorPicker") { showPicker ->
                    if (showPicker) {
                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            imageVector = Icons.Default.Close,
                            contentDescription = iconDescription,
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.ic_color_picker),
                            contentDescription = iconDescription,
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
            }
        }
    }
}

/**
 * Create anchor node at the center of the screen
 */
private fun Frame.createCenterAnchorNode(engine: Engine): AnchorNode? =
    getUpdatedPlanes().firstOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }
        ?.let { it.createAnchorOrNull(it.centerPose) }
        ?.let { anchor ->
            AnchorNode(engine, anchor).apply {
                isEditable = false
                isPositionEditable = false
                updateAnchorPose = false
            }
        }

/**
 * Get real world position from the given [x] & [y] coordinates.
 */
private fun Frame.getPose(x: Float, y: Float): Pose? =
    hitTest(x, y).firstOrNull()?.hitPose
