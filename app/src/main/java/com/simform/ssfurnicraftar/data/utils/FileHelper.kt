package com.simform.ssfurnicraftar.data.utils

import android.net.Uri
import java.io.File
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
     * Cache dir location
     */
    val cacheDir: Path

    /**
     * Get model dir where the models are stored
     *
     * @param create Whether to creates directories ensuring that all
     * nonexistent parent directories exist by creating them first.
     * @return Path for the models directory
     */
    fun getModelDir(create: Boolean = true): Path

    /**
     * Get image dir where the shareable images are stored
     *
     * @param create Whether to creates directories ensuring that all
     * nonexistent parent directories exist by creating them first.
     * @return Path for the images directory
     */
    fun imageShareDir(create: Boolean = true): Path

    /**
     * Get uri for given [file]
     *
     * @param file Uri will be created for this file
     * @return Return uri for file, null if failed to create uri
     */
    fun getUriForFile(file: File): Uri?
}
