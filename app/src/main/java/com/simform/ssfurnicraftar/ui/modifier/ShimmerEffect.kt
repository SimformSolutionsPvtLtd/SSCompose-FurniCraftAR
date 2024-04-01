package com.simform.ssfurnicraftar.ui.modifier

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.simform.ssfurnicraftar.ui.theme.LocalDimens


/**
 * Shimmer effect modifier
 *
 * This modifier is used to display shimmer effect when data is loading.
 *
 * @param color the color used to display as shimmer effect.
 */
@Composable
fun Modifier.shimmerEffect(
    color: Color = MaterialTheme.colorScheme.secondaryContainer
): Modifier = composed {

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    // Make animation infinite
    val transition = rememberInfiniteTransition(label = "Shimmer")

    // Offset representing starting position for the effect
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = LocalDimens.ShimmerEffect.AnimationDuration
            )
        ),
        label = "Shimmer"
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                color.copy(alpha = LocalDimens.ShimmerEffect.ColorAlpha),
                color,
                color.copy(alpha = LocalDimens.ShimmerEffect.ColorAlpha),
                color
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        ),
        shape = CardDefaults.shape
    )
        .onGloballyPositioned {
            // Set measured size for use in effect.
            size = it.size
        }
}
