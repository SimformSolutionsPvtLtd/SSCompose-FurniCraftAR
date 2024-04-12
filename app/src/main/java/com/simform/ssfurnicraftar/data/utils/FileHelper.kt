package com.simform.ssfurnicraftar.data.utils

import java.nio.file.Path

/**
 * File helpers provide helper methods to work with local file system
 */
interface FileHelper {

    /**
     * Folder location
     */
    val filesDir: Path

    /**
     * Get model dir where the models are stored
     *
     * @param create Whether to creates directories ensuring that all
     * nonexistent parent directories exist by creating them first.
     * @return Path for the models directory
     */
    fun getModelDir(create: Boolean = true): Path
}
