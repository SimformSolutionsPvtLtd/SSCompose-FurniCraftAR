package com.simform.ssfurnicraftar.ui.arview

import android.net.Uri

data class ARViewUiState(
    val productId: String,
    val modelPath: String,
    val modelColor: ColorState = ColorState.None,
    val shareUri: Uri? = null
)
