package com.simform.ssfurnicraftar.utils.constant

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

/**
 * API URLs collection.
 */
object Urls {

    // SSFurniCraftAR
    private const val BASE_SSFURNICRAFTAR = "https://ssfurnicraftar.vercel.app"
    private const val MODEL = "$BASE_SSFURNICRAFTAR/models"
    private const val MODEL_COLOR_ARG = "modelColor"

    // SketchFab
    private const val BASE = "https://api.sketchfab.com/v3/"

    fun getBaseUrl() = BASE

    fun getModelUrl(modelId: String, color: Color? = null) = "$MODEL/$modelId".let { modelUrl ->
        color?.let { "${modelUrl}?$MODEL_COLOR_ARG=${color.toArgb()}" } ?: modelUrl
    }
}
