package com.simform.ssfurnicraftar.ui.arview

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simform.ssfurnicraftar.data.utils.FileHelper
import com.simform.ssfurnicraftar.ui.arview.navigation.ARViewArgs
import com.simform.ssfurnicraftar.utils.constant.Constants
import com.simform.ssfurnicraftar.utils.extension.saveToFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Calendar
import javax.inject.Inject
import kotlin.io.path.div

@HiltViewModel
class ARViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val fileHelper: FileHelper
) : ViewModel() {

    private val args = ARViewArgs(savedStateHandle)

    private val _arViewUiState = MutableStateFlow(ARViewUiState(
        productId = args.productId,
        modelPath = getModelPath(args.productId).asStringPath(),
        modelColor = args.modelColor?.let { Color(it) }
    ))
    val arViewUiState = _arViewUiState.asStateFlow()

    /**
     * Change model color to new color or default.
     *
     * @param color The new color to apply. When passed
     * null default model color will be used.
     */
    fun changeColor(color: Color?) {
        _arViewUiState.update { it.copy(modelColor = color) }
    }

    /**
     * Create shareable uri for given [bitmap]
     *
     * @param bitmap The image bitmap to share
     */
    fun createShareUri(bitmap: Bitmap) {
        val fileName = "${Calendar.getInstance().timeInMillis}.${Constants.IMAGE_FILE_EXTENSION}"
        val filePath = fileHelper.imageShareDir() / Paths.get(fileName)

        viewModelScope.launch {
            if (bitmap.saveToFile(filePath)) {
                _arViewUiState.update {
                    it.copy(shareUri = fileHelper.getUriForFile(filePath.toFile()))
                }
            }
        }
    }

    /**
     * Get local model path from given [productId].
     */
    private fun getModelPath(productId: String): Path =
        fileHelper.getModelDir().resolve("${productId}.${Constants.MODEL_EXTENSION}")

    private fun Path.asStringPath(): String = toUri().toString()
}
