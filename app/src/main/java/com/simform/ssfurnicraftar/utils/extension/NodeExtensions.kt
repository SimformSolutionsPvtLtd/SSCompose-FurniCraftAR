package com.simform.ssfurnicraftar.utils.extension

import com.simform.ssfurnicraftar.utils.constant.Constants
import io.github.sceneview.math.Scale
import io.github.sceneview.node.ModelNode

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
