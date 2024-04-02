package com.simform.ssfurnicraftar.ui.arview

import android.view.MotionEvent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.filament.Engine
import com.google.android.filament.MaterialInstance
import com.google.ar.core.CameraConfig
import com.google.ar.core.CameraConfigFilter
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.TrackingFailureReason
import com.simform.ssfurnicraftar.R
import com.simform.ssfurnicraftar.ui.component.ColorPicker
import com.simform.ssfurnicraftar.ui.component.QuitDialog
import com.simform.ssfurnicraftar.ui.theme.LocalDimens
import com.simform.ssfurnicraftar.utils.constant.Constants
import com.simform.ssfurnicraftar.utils.extension.clone
import com.simform.ssfurnicraftar.utils.extension.enableGestures
import com.simform.ssfurnicraftar.utils.extension.setColor
import com.simform.ssfurnicraftar.utils.saver.ColorSaver
import dev.romainguy.kotlin.math.Float3
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.position
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
import java.util.EnumSet
import io.github.sceneview.math.Color as SVColor

@Composable
fun ARViewRoute(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: ARViewViewModel = hiltViewModel()
) {
    val arViewUiState by viewModel.arViewUiState.collectAsStateWithLifecycle()

    ARViewScreen(
        modifier = modifier,
        arViewUiState = arViewUiState,
        onColorChange = viewModel::changeColor,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun ARViewScreen(
    modifier: Modifier = Modifier,
    arViewUiState: ARViewUiState,
    onColorChange: (Color?) -> Unit,
    onNavigateBack: () -> Unit
) {
    // Handle back press and show confirmation dialog when quiting AR View
    var showQuitDialog by remember { mutableStateOf(false) }
    BackHandler {
        showQuitDialog = !showQuitDialog
    }

    Box(modifier = modifier) {
        ARView(
            arViewUiState = arViewUiState
        )

        Options(onColorChange = onColorChange)

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
    arViewUiState: ARViewUiState
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

    var modelPlaced by remember { mutableStateOf(false) }
    var originalMaterials by remember { mutableStateOf<List<List<MaterialInstance>>?>(null) }
    var colorMaterials by remember { mutableStateOf<List<List<MaterialInstance>>?>(null) }

    LaunchedEffect(key1 = arViewUiState.modelPath) {
        model = modelLoader.loadModelInstance(arViewUiState.modelPath)?.let { instance ->
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

    LaunchedEffect(key1 = modelPlaced) {
        if (modelPlaced) {
            model?.apply {
                animate(
                    initialValue = Constants.MODEL_BOUNCING_HEIGHT,
                    targetValue = Constants.MODEL_NO_ROTATION,
                    animationSpec = tween(durationMillis = Constants.MODEL_BOUNCING_DURATION)
                ) { value, _ ->
                    position = Position(y = value)
                }
            }
        }
    }

    LaunchedEffect(key1 = arViewUiState.modelColor, key2 = model) {
        if (arViewUiState.modelColor == null) {
            originalMaterials?.let { model?.materialInstances = it }
            return@LaunchedEffect
        }
    }

    arViewUiState.modelColor?.let { (r, g, b, a) ->
        colorMaterials?.let { model?.materialInstances = it }
        model?.setColor(SVColor(r, g, b, a))
    }

    ARScene(
        modifier = modifier.fillMaxSize(),
        childNodes = childNodes,
        engine = engine,
        view = view,
        modelLoader = modelLoader,
        onGestureListener = rememberOnGestureListener(
            onSingleTapConfirmed = { event, node ->
                // If no model is placed then create anchor node and add
                // model node to that anchor
                if (childNodes.isEmpty()) {
                    val anchorNode =
                        frame?.let { frame -> createAnchorNode(engine, frame, event) }
                            ?: return@rememberOnGestureListener
                    model?.let {
                        modelPlaced = true
                        anchorNode.addChildNode(it)
                        childNodes.add(anchorNode)
                        planeRenderer = false
                    }
                }
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
            trackingFailureReason = it
        },
        onSessionUpdated = { session, updatedFrame ->
            frame = updatedFrame
        }
    )
}

@Composable
private fun Options(
    modifier: Modifier = Modifier,
    onColorChange: (Color?) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(LocalDimens.SpacingMedium)
    ) {
        ColorOption(
            modifier = Modifier.align(Alignment.BottomCenter),
            onSelect = onColorChange
        )
    }
}

@Composable
private fun ColorOption(
    modifier: Modifier = Modifier,
    onSelect: (Color?) -> Unit
) {
    var showPicker by rememberSaveable { mutableStateOf(false) }
    var selectedColor by rememberSaveable(stateSaver = ColorSaver) { mutableStateOf(null) }

    BoxWithConstraints(modifier = modifier) {
        Row(
            modifier = Modifier
                .height(LocalDimens.ARView.OptionsIconSize)
                .clip(CircleShape)
        ) {
            AnimatedVisibility(showPicker) {
                Row(
                    modifier = Modifier.widthIn(
                        max = this@BoxWithConstraints.maxWidth - LocalDimens.ARView.OptionsIconSize - (LocalDimens.SpacingSmall * 2)
                    ),
                    horizontalArrangement = Arrangement.spacedBy(LocalDimens.SpacingSmall)
                ) {
                    Button(
                        modifier = Modifier.size(LocalDimens.ARView.OptionsIconSize),
                        onClick = {
                            selectedColor = null
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
                        onSelect = { color ->
                            selectedColor = color
                            onSelect(selectedColor)
                        }
                    )
                }
            }

            Button(
                modifier = Modifier
                    .padding(horizontal = LocalDimens.SpacingSmall)
                    .size(LocalDimens.ARView.OptionsIconSize),
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
 * Create anchor node at the real world location where the touch event has occurred.
 */
private fun createAnchorNode(engine: Engine, frame: Frame, event: MotionEvent): AnchorNode? =
    frame.hitTest(event).firstOrNull()?.createAnchorOrNull()?.let { anchor ->
        AnchorNode(engine, anchor)
    }
