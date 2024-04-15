package com.simform.ssfurnicraftar.utils.extension

import android.graphics.Bitmap
import androidx.palette.graphics.Palette
import com.simform.ssfurnicraftar.utils.constant.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.outputStream

/**
 * Save the [Bitmap] at given [path].
 *
 * Saved file can be used to share using [androidx.core.content.FileProvider].
 *
 * @param path The path where bitmap will be stored.
 * @return Whether the file save succeeded or not.
 */
suspend fun Bitmap.saveToFile(path: Path): Boolean = withContext(Dispatchers.IO) {
    try {
        path.outputStream(StandardOpenOption.CREATE).use { outStream ->
            this@saveToFile.compress(Bitmap.CompressFormat.JPEG, Constants.IMAGE_COMPRESSION_QUALITY, outStream)
        }
        true
    } catch (ioException: IOException) {
        Timber.e("Failed to save bitmap. ${ioException.localizedMessage}")
        false
    }
}

/**
 * Get vibrant color from the receiver [Bitmap].
 *
 * If it fails to get vibrant color, `Black` Color will be returned
 * as a default value.
 *
 */
fun Bitmap.getVibrantColor() =
    Palette.from(this).generate().getDarkVibrantColor(0x000000)
