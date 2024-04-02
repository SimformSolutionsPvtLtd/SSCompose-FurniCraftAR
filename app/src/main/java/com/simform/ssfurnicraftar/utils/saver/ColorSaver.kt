package com.simform.ssfurnicraftar.utils.saver

import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

/**
 * Color saver used by [androidx.compose.runtime.saveable.rememberSaveable]
 * to save color in bundle
 */
val ColorSaver = Saver<Color?, Int>(
    save = { color -> color?.toArgb() },
    restore = { argb -> Color(argb) }
)
