package com.simform.ssfurnicraftar.ui.theme

import androidx.compose.ui.unit.dp

@Suppress("ConstPropertyName", "unused")
object LocalDimens {

    // Standard spacing
    val NoSpacing = 0.dp
    val SpacingXXS = 2.dp
    val SpacingXS = 4.dp
    val SpacingSmall = 8.dp
    val SpacingMedium = 16.dp
    val SpacingLarge = 24.dp
    val SpacingXL = 32.dp
    val SpacingXXL = 48.dp
    val SpacingXXXL = 72.dp

    // Standard height
    val HeightSmall = 48.dp
    val HeightMedium = 56.dp
    val HeightLarge = 72.dp
    val HeightXL = 120.dp

    // Standard icon size
    val IconXXS = 18.dp
    val IconXS = 24.dp
    val IconSmall = 32.dp
    val IconMedium = 48.dp
    val IconLarge = 64.dp
    val IconXL = 80.dp
    val IconXXL = 100.dp
    val IconXXXL = 125.dp

    // Product Screen
    object Products {
        const val CardRatio = 3f / 4f
        const val PlaceholdersCount = 10
        const val PlaceholderAlpha = 0.75f
        val CardSize = 140.dp
    }

    // Download Screen
    object Download {
        const val ProgressWidthPercentage = 0.7F
    }

    object ARView {
        val OptionsIconSize = 44.dp
        const val MODEL_PLACEMENT_WIDTH_PROPORTION = 2F
        const val MODEL_PLACEMENT_HEIGHT_PROPORTION = 2F
        const val GestureAnimRatio = 1F
        val ShareOptionSpacing = 12.dp
        const val OverlayAlpha = 0.5F
    }

    // ShimmerEffect
    object ShimmerEffect {
        const val AnimationDuration = 1000
        const val ColorAlpha = 0.5f
    }

    // Color Picker
    object ColorPicker {
        val SelectionBorderSize = 2.dp
        val NoSelectionBorderSize = 0.dp
    }
}
