package com.simform.ssfurnicraftar.utils.extension

import android.graphics.Bitmap
import android.os.Handler
import android.os.HandlerThread
import android.view.PixelCopy
import com.simform.ssfurnicraftar.utils.constant.Constants
import io.github.sceneview.ar.ARSceneView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Capture image from [ARSceneView] using [PixelCopy].
 *
 * @return Returns the [Bitmap] of view.
 */
fun ARSceneView.captureImage(): Bitmap {
    // Create a bitmap the size of the scene view.
    val bitmap = Bitmap.createBitmap(
        width, height,
        Bitmap.Config.ARGB_8888
    )

    // Create a handler thread to offload the processing of the image.
    val handlerThread = HandlerThread(Constants.IMAGE_CAPTURE_HANDLER_NAME)
    handlerThread.start()

    PixelCopy.request(this, bitmap, { copyResult ->
        if (copyResult == PixelCopy.SUCCESS) {
            Timber.d("Created bitmap from ARSceneView success.")
        } else {
            Timber.e("Failed to create bitmap from ARSceneView.")
        }
        handlerThread.quitSafely()
    }, Handler(handlerThread.looper))

    return bitmap
}