package com.simform.ssfurnicraftar.utils.extension

import com.simform.ssfurnicraftar.utils.constant.Constants
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import io.github.sceneview.material.setBaseColorFactor
import io.github.sceneview.material.setBaseColorIndex
import io.github.sceneview.math.Color
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Scale
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node

/**
 * Set model's color
 *
 * If model is divided in sub-mesh then color will be applied
 * recursively.
 *
 * @param color The new color to set on model
 */
fun ModelNode.setColor(color: Color) {
    materialInstances.setColor(color)
}

/**
 * Enable gestures for receiver [ModelNode].
 *
 * The default gestures for scale has exponential effect.
 * So using custom gesture for scaling is better.
 */
fun ModelNode.enableGestures() {
    var previousScale = Constants.MODEL_NO_SCALE
    onScale = { _, _, value ->
        scale = Scale(previousScale * value)
        false
    }
    onScaleBegin = { _, _ ->
        previousScale = scale.x
        false
    }
    onScaleEnd = { _, _ ->
        previousScale = Constants.MODEL_NO_SCALE
    }

    // Set rendering priority higher to properly load occlusion.
    setPriority(Constants.MODEL_RENDER_LOWEST_PRIORITY)
    // Model Node needs to be editable for independent rotation from the anchor rotation
    isEditable = true
    isScaleEditable = true
    isRotationEditable = true
    editableScaleRange = Float.MIN_VALUE..Float.MAX_VALUE
}

/**
 * Start bouncing effect on receiver [Node].
 *
 * @param targetValue The value in meter how much node will be bouncing.
 */
suspend fun Node.startBouncingEffect(
    targetValue: Float = Constants.MODEL_BOUNCING_HEIGHT
) {
    animate(
        initialValue = Constants.MODEL_NO_HEIGHT,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = Constants.MODEL_BOUNCING_DURATION),
            repeatMode = RepeatMode.Reverse
        )
    ) { value, _ ->
        position = Position(y = value)
    }
}

/**
 * End bouncing effect on receiver [Node].
 */
suspend fun Node.endBouncingEffect() {
    animate(
        initialValue = position.y,
        targetValue = Constants.MODEL_NO_HEIGHT,
        animationSpec = tween()
    ) { value, _ ->
        position = Position(y = value)
    }
}

/**
 * Start rotation effect on [Node].
 *
 * @param duration The time indicating speed of rotation. i.e. duration in which
 * single rotation will be completed.
 */
suspend fun Node.startModelRotation(
    duration: Int = Constants.MODEL_ROTATION_DURATION
) {
    animate(
        initialValue = rotation.y,
        targetValue = rotation.y + Constants.MODEL_360_ROTATION,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = duration, easing = LinearEasing)
        )
    ) { value, _ ->
        rotation = Rotation(y = value)
    }
}
