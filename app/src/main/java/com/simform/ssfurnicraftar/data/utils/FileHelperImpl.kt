package com.simform.ssfurnicraftar.data.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.simform.ssfurnicraftar.BuildConfig
import com.simform.ssfurnicraftar.utils.constant.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import javax.inject.Inject
import kotlin.io.path.createDirectories
import kotlin.io.path.div

private const val MODEL_DIR = "models"
private const val SHARED_IMAGE_DIR = "shared_images"

class FileHelperImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : FileHelper {

    private val modelDirPath = Paths.get(MODEL_DIR)
    private val sharedImageDir = Paths.get(SHARED_IMAGE_DIR)
    private val providerAuthority = "${BuildConfig.APPLICATION_ID}.${Constants.PROVIDER_EXTENSION}"

    override val filesDir: Path
        get() = context.filesDir.toPath()

    override val cacheDir: Path
        get() = context.cacheDir.toPath()

    override fun getModelDir(create: Boolean): Path =
        (context.filesDir.toPath() / modelDirPath).apply {
            if (create) {
                createDirectories()
            }
        }

    override fun imageShareDir(create: Boolean): Path =
        (context.cacheDir.toPath() / sharedImageDir).apply {
            if (create) {
                createDirectories()
            }
        }

    override fun getUriForFile(file: File): Uri? =
        if (file.exists()) {
            try {
                FileProvider.getUriForFile(context, providerAuthority, file)
            } catch (securityException: SecurityException) {
                Timber.d("Can't create provider for file ${file.absolutePath}.")
                null
            }
        } else {
            Timber.d("File ${file.absolutePath} doesn't exist.")
            null
        }
}
