package com.simform.ssfurnicraftar.ui.arview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.simform.ssfurnicraftar.data.utils.FileHelper
import com.simform.ssfurnicraftar.ui.arview.navigation.ARViewArgs
import com.simform.ssfurnicraftar.utils.constant.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.nio.file.Path
import javax.inject.Inject

@HiltViewModel
class ARViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val fileHelper: FileHelper
) : ViewModel() {

    private val args = ARViewArgs(savedStateHandle)

    private val _arViewUiState = MutableStateFlow(
        ARViewUiState(
            productId = args.productId,
            modelPath = getModelPath(args.productId).asStringPath()
        )
    )
    val arViewUiState = _arViewUiState.asStateFlow()

    /**
     * Get local model path from given [productId].
     */
    private fun getModelPath(productId: String): Path =
        fileHelper.getModelDir().resolve("${productId}.${Constants.MODEL_EXTENSION}")

    private fun Path.asStringPath(): String = toUri().toString()
}
