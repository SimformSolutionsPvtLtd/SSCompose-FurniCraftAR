package com.simform.ssfurnicraftar.data.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.nio.file.Path
import java.nio.file.Paths
import javax.inject.Inject
import kotlin.io.path.createDirectories
import kotlin.io.path.div

private const val MODEL_DIR = "models"

class FileHelperImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : FileHelper {

    private val modelDirPath = Paths.get(MODEL_DIR)

    override val filesDir: Path
        get() = context.filesDir.toPath()

    override fun getModelDir(create: Boolean): Path =
        (context.filesDir.toPath() / modelDirPath).apply {
            if (create) {
                createDirectories()
            }
        }
}
