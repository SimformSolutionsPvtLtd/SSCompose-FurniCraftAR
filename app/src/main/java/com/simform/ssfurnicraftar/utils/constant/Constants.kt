package com.simform.ssfurnicraftar.utils.constant

object Constants {

    /**
     * Database
     */
    const val DATABASE = "ssfurnicraftar-database"
    const val TABLE_PRODUCT = "product"
    const val TABLE_CATEGORY = "category"
    const val TABLE_CATEGORY_PRODUCT = "category_product"
    const val TABLE_REMOTE_KEY = "remote_key"

    /**
     * Authenticator
     */
    const val HEADER_AUTHORIZATION = "Authorization"
    const val TOKEN_KEY = "Token"

    /**
     * Download
     */
    const val DOWNLOAD_PROGRESS_INTERVAL = 500 // MS
    const val LABEL_DOWNLOAD_PROGRESS = "DownloadProgress"

    /**
     * Model
     */
    const val MODEL_EXTENSION = "glb"
    const val MODEL_INITIAL_SIZE = 0.5F // Meter
    const val MODEL_BOUNCING_HEIGHT = 0.05F // Meter
    const val MODEL_BOUNCING_DURATION = 1000 // MS
    const val MODEL_NO_ROTATION = 0F // Degree
    const val MODEL_360_ROTATION = 360F // Degree
    const val MODEL_ROTATION_DURATION = 10000 // MS
    const val MODEL_NO_SCALE = 0F
    const val MODEL_NO_HEIGHT = 0F // Meter
    const val MODEL_RENDER_LOWEST_PRIORITY = 7
    const val MODEL_BASE_COLOR_INDEX = 0

    /**
     * Gesture
     */
    const val DEFAULT_GESTURE_OVERLAY_ITERATIONS = 1
    const val PLAY_COUNT_ZERO = 0

    /**
     * Provider
     */
    const val PROVIDER_EXTENSION = "provider"

    /**
     * Image
     */
    const val IMAGE_TYPE = "image/jpeg"
    const val IMAGE_COMPRESSION_QUALITY = 100 // Percentage
    const val IMAGE_CAPTURE_HANDLER_NAME = "PixelCopier"
    const val IMAGE_FILE_EXTENSION = "jpeg"

    /**
     * Animation
     */
    const val ANIM_COMPLETE_VALUE = 1F

    /**
     * Color Picker
     */
    const val ANIM_LABEL_COLOR_PICKER = "ColorPickerAnimation"
}
