package com.simform.ssfurnicraftar.ui.arview.component

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.filament.Engine
import com.google.android.filament.MaterialInstance
import com.google.ar.core.CameraConfig
import com.google.ar.core.CameraConfigFilter
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.Pose
import com.google.ar.core.Session
import com.google.ar.core.TrackingFailureReason
import com.simform.ssfurnicraftar.ui.arview.ARViewUiState
import com.simform.ssfurnicraftar.ui.theme.LocalDimens
import com.simform.ssfurnicraftar.utils.constant.Constants
import com.simform.ssfurnicraftar.utils.extension.captureImage
import com.simform.ssfurnicraftar.utils.extension.clone
import com.simform.ssfurnicraftar.utils.extension.enableGestures
import com.simform.ssfurnicraftar.utils.extension.endBouncingEffect
import com.simform.ssfurnicraftar.utils.extension.setColor
import com.simform.ssfurnicraftar.utils.extension.startBouncingEffect
import com.simform.ssfurnicraftar.utils.extension.startModelRotation
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.getUpdatedPlanes
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
internal fun ARView(
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
                // Change models initial scale. Currently using width
                scale = Scale(Constants.MODEL_INITIAL_SIZE / size.x)
                // Reset model's initial rotation
                rotation = Rotation()
                // Enable custom gestures on model
                enableGestures()
                // Keep reference to original material to reset color
                originalMaterials = materialInstances
                // Clone materials to apply new colors
                colorMaterials = materialInstances.clone()
            }
        }
    }

    LaunchedEffect(key1 = animateModel) {
        model?.apply {
            if (animateModel) startBouncingEffect() else endBouncingEffect()
        }
    }

    LaunchedEffect(key1 = rotationEnabled) {
        if (rotationEnabled) {
            model?.startModelRotation()
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
                onSingleTapConfirmed = { _, _ ->
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
            sessionCameraConfig = { session -> session.disableDepthConfig() },
            sessionConfiguration = { _, config -> config.configure() },
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

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.Center)
                .background(Color.Black.copy(alpha = LocalDimens.ARView.OverlayAlpha)),
            visible = showCoachingOverlay,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ARCoachingOverlay(
                failureReason = trackingFailureReason ?: TrackingFailureReason.NONE
            )
        }

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.Center)
                .background(Color.Black.copy(alpha = LocalDimens.ARView.OverlayAlpha)),
            visible = showGestureOverlay,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            GestureOverlay {
                showGestureOverlay = false
            }
        }
    }
}

/**
 * Disable Depth API
 * Depth API may give better results. But, some devices (tested on Samsung S20+)
 * creates too much lag. Reason is unknown. Looks like the image acquired
 * with `frame.acquireCameraImage()` is not realised. so we won't be able to acquire
 * new frames when the rate limit is exceeded.
 */
private fun Session.disableDepthConfig(): CameraConfig {
    val filter = CameraConfigFilter(this)
    filter.setDepthSensorUsage(EnumSet.of(CameraConfig.DepthSensorUsage.DO_NOT_USE))
    val cameraConfigList = getSupportedCameraConfigs(filter)
    return cameraConfigList.first()
}

/**
 * Configurations for ARSession
 */
private fun Config.configure() {
    depthMode = Config.DepthMode.DISABLED
    updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
    instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
    planeFindingMode = Config.PlaneFindingMode.HORIZONTAL
    lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
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
