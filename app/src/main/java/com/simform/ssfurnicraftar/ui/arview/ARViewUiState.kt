package com.simform.ssfurnicraftar.ui.arview

import android.net.Uri
import androidx.compose.ui.graphics.Color

data class ARViewUiState(
    val productId: String,
    val modelPath: String,
    val modelColor: Color? = null,
    val shareUri: Uri? = null
)
