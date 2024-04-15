package com.simform.ssfurnicraftar.utils.constant

import com.simform.ssfurnicraftar.ui.arview.ColorState

/**
 * API URLs collection.
 */
object Urls {

    /**
     * SSFurniCraftAR
     */
    const val BASE_SSFURNICRAFTAR = "https://ssfurnicraftar.vercel.app"
    const val MODEL = "$BASE_SSFURNICRAFTAR/models"
    private const val MODEL_COLOR_ARG = "modelColor"

    /**
     * SketchFab
     */
    private const val BASE = "https://api.sketchfab.com/v3/"

    fun getBaseUrl() = BASE

    /**
     * Get furniture model url with optional color value
     *
     * @param modelId The furniture model id to share
     * @param color The [ColorState] option to share
     */
    fun getModelUrl(modelId: String, color: ColorState = ColorState.None) =
        "$MODEL/$modelId".let { modelUrl ->
            color.stringValue?.let { "${modelUrl}?$MODEL_COLOR_ARG=$it" } ?: modelUrl
        }
}
